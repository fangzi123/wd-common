package com.wdcloud.mq.handler;

import com.wdcloud.mq.codec.ICodecFactory;
import com.wdcloud.mq.core.MqMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Andy
 * @date 17/4/17
 * <p>
 * MessageAdapterHandler
 * <p>消息处理适配器，主要功能：</p>
 * <p>1、将不同的消息类型绑定到对应的处理器并缓存，如将queue+exchange的消息统一交由A处理器来出来</p>
 * <p>2、执行消息的消费分发，调用相应的处理器来消费属于它的消息</p>
 */
public class MessageAdapterHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageAdapterHandler.class);

    private ConcurrentMap<String, MessageProcessorWrap> processorWrapConcurrentMap;
    private ConcurrentMap<String, String> processorExchangeQueueMap;

    public MessageAdapterHandler() {
        this.processorWrapConcurrentMap = new ConcurrentHashMap<>();
        this.processorExchangeQueueMap = new ConcurrentHashMap<>();
    }

    public void handleMessage(MqMessage message) {
        logger.debug("Receive an EventMessage: [" + message + "]");
        // 先要判断接收到的message是否是空的，在某些异常情况下，会产生空值
        if (message == null) {
            logger.warn("Receive an null EventMessage, it may product some errors, and processing message is canceled.");
            return;
        }
        if (StringUtils.isEmpty(message.getExchangeName())) {
            logger.warn("The EventMessage's  exchangeName is empty, this is not allowed, and processing message is canceled.");
            return;
        }

        MessageProcessorWrap processorWrap;
        // 解码，并交给对应的EventHandle执行
        // 当队列名为空时，认为是广播模式的消息
        if (StringUtils.isEmpty(message.getQueueName())) {
            processorWrap = processorWrapConcurrentMap.get(message.getExchangeName());
        } else {
            processorWrap = processorWrapConcurrentMap.get(message.getQueueName() + "|" + message.getExchangeName());
        }
        if (processorWrap == null) {
            logger.warn("Receive an EopEventMessage, but no processor can do it.");
            return;
        }
        try {
            processorWrap.process(message.getMsgBytes());
        } catch (IOException e) {
            logger.error("Event content can not be Deserialized, check the provided ICodecFactory.", e);
            return;
        }
    }

    /**
     * 增加监听
     *
     * @param queueName      队列名称
     * @param exchangeName   交换
     * @param messageHandler 消息处理实现
     * @param codecFactory   序列化实现
     */
    public void add(String queueName, String exchangeName, IMqMessageHandler messageHandler, ICodecFactory codecFactory) {
        this.add(queueName, exchangeName, messageHandler, codecFactory, false);
    }

    /**
     * 增加监听
     *
     * @param queueName      队列名称
     * @param exchangeName   交换
     * @param messageHandler 消息处理实现
     * @param codecFactory   序列化实现
     * @param isFanout       是否是广播 true:是 false:否
     */
    public void add(String queueName, String exchangeName, IMqMessageHandler messageHandler, ICodecFactory codecFactory, boolean isFanout) {
        if (StringUtils.isEmpty(exchangeName) || messageHandler == null || codecFactory == null) {
            throw new RuntimeException("exchangeName can not be empty,and processor or codecFactory can not be null. ");
        }
        MessageProcessorWrap epw = new MessageProcessorWrap(codecFactory, messageHandler);
        if (isFanout) {
            addProcessorWrap(exchangeName, epw);
        } else {
            addProcessorWrap(queueName + "|" + exchangeName, epw);
        }

        processorExchangeQueueMap.putIfAbsent(queueName + "|" + exchangeName, queueName);
    }

    private void addProcessorWrap(String key, MessageProcessorWrap processorWrap) {
        MessageProcessorWrap epw = processorWrapConcurrentMap.putIfAbsent(key, processorWrap);
        if (epw != null) {
            logger.warn("The processor of this queue and exchange exists, and the new one can't be add");
        }
    }

    public Set<String> getAllBindings() {
        return processorExchangeQueueMap.keySet();
    }


    protected static class MessageProcessorWrap {

        private ICodecFactory codecFactory;

        private IMqMessageHandler messageHandler;

        protected MessageProcessorWrap(ICodecFactory codecFactory,
                                       IMqMessageHandler messageHandler) {
            this.codecFactory = codecFactory;
            this.messageHandler = messageHandler;
        }

        public void process(byte[] eventData) throws IOException {
            Object obj = codecFactory.deSerialize(eventData);
            messageHandler.processMsg(obj);
        }
    }
}
