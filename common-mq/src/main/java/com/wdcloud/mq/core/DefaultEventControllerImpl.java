package com.wdcloud.mq.core;

import com.wdcloud.mq.codec.DefaultCodecImpl;
import com.wdcloud.mq.codec.ICodecFactory;
import com.wdcloud.mq.exception.MQSendMessageException;
import com.wdcloud.mq.handler.IMqMessageHandler;
import com.wdcloud.mq.handler.MessageAdapterHandler;
import com.wdcloud.mq.handler.MessageErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Andy
 * @date 17/4/17
 * <p>
 * <p>
 * 和rabbitmq通信的控制器，主要负责：
 * <p>1、和rabbitmq建立连接</p>
 * <p>2、声明exChange和queue以及它们的绑定关系</p>
 * <p>3、启动消息监听容器，并将不同消息的处理者绑定到对应的exchange和queue上</p>
 * <p>4、持有消息发送模版以及所有exchange、queue和绑定关系的本地缓存</p>
 */
public class DefaultEventControllerImpl implements IEventController {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageSendServiceImpl.class);

    private CachingConnectionFactory rabbitConnectionFactory;

    private MqConfig config;

    private RabbitAdmin rabbitAdmin;

    private ICodecFactory defaultCodecFactory = new DefaultCodecImpl();

    private SimpleMessageListenerContainer msgListenerContainer; // rabbitMQ msg listener container

    private MessageAdapterHandler msgAdapterHandler = new MessageAdapterHandler();

    private MessageConverter serializerMessageConverter = new SerializerMessageConverter(); // 直接指定
    //queue cache, key is exchangeName
    private Map<String, Exchange> exchanges = new HashMap<>();
    //queue cache, key is queueName
    private Map<String, Queue> queues = new HashMap<>();
    //bind relation of queue to exchange cache, value is exchangeName | queueName
    private Set<String> binded = new HashSet<>();

    private IMessageSendService messageSendService; // 给App使用的Event发送客户端

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    private static DefaultEventControllerImpl defaultEventController;

    public synchronized static DefaultEventControllerImpl getInstance(MqConfig config) {
        if (config == null) {
            logger.warn("[DefaultEventController] getInstance of DefaultEventControllerImpl error.msg={}", "config is null");
            throw new IllegalArgumentException("Config can not be null.");
        }
        logger.info("[DefaultEventController] getInstance of DefaultEventControllerImpl.config={}", config.toString());
        if (defaultEventController == null) {
            defaultEventController = new DefaultEventControllerImpl(config);
        }
        return defaultEventController;
    }

    private DefaultEventControllerImpl(MqConfig config) {
        this.config = config;
        initRabbitConnectionFactory();
        // 初始化AmqpAdmin
        rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory);
        // 初始化RabbitTemplate
        RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
        rabbitTemplate.setMessageConverter(serializerMessageConverter);
        messageSendService = new DefaultMessageSendServiceImpl(rabbitTemplate, defaultCodecFactory, this);
    }

    /**
     * 初始化rabbitmq连接
     */
    private void initRabbitConnectionFactory() {
        rabbitConnectionFactory = new CachingConnectionFactory();
        rabbitConnectionFactory.setAddresses(config.getAddresses());
        rabbitConnectionFactory.setChannelCacheSize(config.getEventMsgProcessNum());
        rabbitConnectionFactory.setUsername(config.getUsername());
        rabbitConnectionFactory.setPassword(config.getPassword());
        if (!StringUtils.isEmpty(config.getVirtualHost())) {
            rabbitConnectionFactory.setVirtualHost(config.getVirtualHost());
        }
    }

    /**
     * 注销程序
     */
    public synchronized void destroy() {
        logger.info("[DefaultEventController] destroy DefaultEventControllerImpl");

        if (!isStarted.get()) {
            return;
        }
        msgListenerContainer.stop();
        messageSendService = null;
        rabbitAdmin = null;
        rabbitConnectionFactory.destroy();
    }

    @Override
    public void start() {
        start(null);
    }

    @Override
    public void start(Exchange... exchangeArray) {
        if (isStarted.get()) {
            logger.info("[DefaultEventController] is already started");
            return;
        }
        Set<String> mapping = msgAdapterHandler.getAllBindings();
        if (mapping == null || mapping.isEmpty()) {
            if (exchangeArray != null && exchangeArray.length > 0) {
                for (Exchange exchange : exchangeArray) {
                    exchanges.put(exchange.getName(), exchange);
                    rabbitAdmin.declareExchange(exchange);//声明exchange
                }
            }
        } else {
            for (String relation : mapping) {
                String[] relaArr = relation.split("\\|");
                boolean topicFlag = true;
                if (exchangeArray != null && exchangeArray.length > 0) {
                    for (Exchange exchange : exchangeArray) {
                        if (relaArr[1].equals(exchange.getName())) {
                            declareBinding(relaArr[1], relaArr[0], exchange);
                            topicFlag = false;
                        }
                    }
                    if (topicFlag) {
                        declareBinding(relaArr[1], relaArr[0]);
                    }
                } else {
                    declareBinding(relaArr[1], relaArr[0]);
                }
            }
        }
        initMsgListenerAdapter();
        isStarted.set(true);
    }

    /**
     * 初始化消息监听器容器
     */
    private void initMsgListenerAdapter() {
        MessageListener listener = new MessageListenerAdapter(msgAdapterHandler, serializerMessageConverter);
        msgListenerContainer = new SimpleMessageListenerContainer();
        msgListenerContainer.setConnectionFactory(rabbitConnectionFactory);
        msgListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        msgListenerContainer.setMessageListener(listener);
        msgListenerContainer.setErrorHandler(new MessageErrorHandler());
        msgListenerContainer.setPrefetchCount(config.getPrefetchSize()); // 设置每个消费者消息的预取值
        msgListenerContainer.setConcurrentConsumers(config.getEventMsgProcessNum());
        msgListenerContainer.setTxSize(config.getPrefetchSize());//设置有事务时处理的消息数
        msgListenerContainer.setQueues(queues.values().toArray(new Queue[queues.size()]));
        msgListenerContainer.start();
    }

    @Override
    public IMessageSendService getEopEventTemplate() {
        logger.info("[DefaultEventController] getEopEventTemplate");
        return messageSendService;
    }

    @Override
    public Queue getDeclareQueue() {
        return rabbitAdmin.declareQueue();
    }

    @Override
    public IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler) {
        logger.info("[DefaultEventController] add bind! queueName={}, exchangeName={}, messageHandler={}", queueName, exchangeName, messageHandler.getClass().getName());
        return add(queueName, exchangeName, messageHandler, defaultCodecFactory, false);
    }

    @Override
    public IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler, ICodecFactory codecFactory) {
        return this.add(queueName, exchangeName, messageHandler, codecFactory, false);
    }

    @Override
    public IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler, boolean isFanout) {
        logger.info("[DefaultEventController] add bind! queueName={}, exchangeName={}, messageHandler={}", queueName, exchangeName, messageHandler.getClass().getName());
        return add(queueName, exchangeName, messageHandler, defaultCodecFactory, isFanout);
    }

    @Override
    public IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler, ICodecFactory codecFactory, boolean isFanout) {
        if (StringUtils.isEmpty(exchangeName) || messageHandler == null || codecFactory == null) {
            logger.warn("[DefaultEventController] bind fail, params error! exchangeName={}, messageHandler={}, codecFactory={}",
                    exchangeName, messageHandler.getClass().getName(), codecFactory.getClass().getName());
            throw new MQSendMessageException("[DefaultEventController] bind fail, params error!");
        }
        logger.info("[DefaultEventController] add bind! queueName={}, exchangeName={}, messageHandler={}", queueName, exchangeName, messageHandler.getClass().getName());

        msgAdapterHandler.add(queueName, exchangeName, messageHandler, codecFactory, isFanout);
        if (isStarted.get()) {
            initMsgListenerAdapter();
        }
        return this;
    }

    /**
     * exchange和queue是否已经绑定
     */
    boolean beBinded(String exchangeName, String queueName) {
        if (StringUtils.isEmpty(queueName) || StringUtils.isEmpty(exchangeName)) {
            logger.warn("[DefaultEventController] get bind fail, params error! queueName={}, exchangeName={}",
                    queueName, exchangeName);
            throw new MQSendMessageException("[DefaultEventController] get bind fail, params error!");
        }

        return binded.contains(exchangeName + "|" + queueName);
    }

    /**
     * 声明exchange和queue已经它们的绑定关系
     */
    synchronized void declareBinding(String exchangeName, String queueName) {
        String bindRelation = exchangeName + "|" + queueName;
        if (binded.contains(bindRelation)) {
            return;
        }

        Exchange exchange = exchanges.get(exchangeName);
        if (exchange == null) {
            exchange = new TopicExchange(exchangeName, true, false, null);
        }
        declareBinding(exchangeName, queueName, exchange);

    }

    /**
     * 声明exchange和queue已经它们的绑定关系
     */
    private synchronized void declareBinding(String exchangeName, String queueName, Exchange exchange) {
        String bindRelation = exchangeName + "|" + queueName;
        if (binded.contains(bindRelation)) return;

        boolean needBinding = false;
        if (exchanges.get(exchangeName) == null) {
            if (exchange != null) {
                exchanges.put(exchangeName, exchange);
                rabbitAdmin.declareExchange(exchange);//声明exchange
                needBinding = true;
            } else {
                exchange = new TopicExchange(exchangeName);
                exchanges.put(exchangeName, exchange);

                rabbitAdmin.declareExchange(exchange);//声明exchange
                needBinding = true;
            }
        } else {
            exchange = exchanges.get(exchangeName);
        }

        Queue queue = queues.get(queueName);
        if (queue == null) {
            queue = new Queue(queueName, true, false, false);
            queues.put(queueName, queue);
            rabbitAdmin.declareQueue(queue);    //声明queue
            needBinding = true;
        }


        if (needBinding) {
            Binding binding = getBinding(exchange, queue, queueName);
            if (binding == null) {
                return;
            }
            rabbitAdmin.declareBinding(binding);//声明绑定关系
            binded.add(bindRelation);

        }
    }

    private Binding getBinding(Exchange exchange, Queue queue, String queueName) {
        Binding binding = null;
        if (exchange instanceof TopicExchange) {
            binding = BindingBuilder.bind(queue).to((TopicExchange) exchange).with(queueName);//将queue绑定到exchange
        } else if (exchange instanceof DirectExchange) {
            binding = BindingBuilder.bind(queue).to((DirectExchange) exchange).with(queueName);//将queue绑定到exchange
        } else if (exchange instanceof CustomExchange) {
            logger.warn("[DefaultEventController] get binding fail, class error. Just support CustomExchange and DirectExchange! class={}",
                    CustomExchange.class.getName());
            throw new MQSendMessageException("[DefaultEventController] get binding fail, not support " + CustomExchange.class.getName());
        } else if (exchange instanceof HeadersExchange) {
            logger.warn("[DefaultEventController] get binding fail, class error. Just support HeadersExchange and DirectExchange! class={}",
                    HeadersExchange.class.getName());
            throw new MQSendMessageException("[DefaultEventController] get binding fail, not support " + HeadersExchange.class.getName());
        } else if (exchange instanceof FanoutExchange) {
            binding = BindingBuilder.bind(queue).to((FanoutExchange) exchange);//将queue绑定到exchange
        }
        return binding;
    }
}