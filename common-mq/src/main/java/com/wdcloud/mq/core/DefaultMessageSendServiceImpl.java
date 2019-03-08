package com.wdcloud.mq.core;

import com.wdcloud.mq.codec.ICodecFactory;
import com.wdcloud.mq.exception.MQSendMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.StringUtils;

/**
 * 消息发送
 *
 * @author Andy
 * @date 17/4/17
 */
public class DefaultMessageSendServiceImpl implements IMessageSendService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageSendServiceImpl.class);

    private AmqpTemplate eventAmqpTemplate;

    private ICodecFactory defaultCodecFactory;

    private DefaultEventControllerImpl defaultEventController;

    public DefaultMessageSendServiceImpl(AmqpTemplate eopAmqpTemplate, ICodecFactory defaultCodecFactory, DefaultEventControllerImpl defaultEventController) {
        this.eventAmqpTemplate = eopAmqpTemplate;
        this.defaultCodecFactory = defaultCodecFactory;
        this.defaultEventController = defaultEventController;
    }

    public DefaultMessageSendServiceImpl(AmqpTemplate eopAmqpTemplate, ICodecFactory defaultCodecFactory) {
        this.eventAmqpTemplate = eopAmqpTemplate;
        this.defaultCodecFactory = defaultCodecFactory;
    }

    @Override
    public void send(String queueName, String exchangeName, Object eventContent) {
        this.send(queueName, exchangeName, eventContent, defaultCodecFactory);
    }

    @Override
    public void send(String queueName, String exchangeName, Object eventContent, ICodecFactory codecFactory) {
        if (StringUtils.isEmpty(queueName) || StringUtils.isEmpty(exchangeName)) {
            logger.warn("[DefaultMessageSendService] queueName or exchangeName is empty. queueName={}, exchangeName={}", queueName, exchangeName);

            throw new MQSendMessageException("queueName and exchangeName can not be empty.");
        }

        if (!defaultEventController.beBinded(exchangeName, queueName)) {
            defaultEventController.declareBinding(exchangeName, queueName);
        }

        if (codecFactory == null) {
            logger.warn("[DefaultMessageSendService] codecFactory must not be null ,unless eventContent is null");
            throw new MQSendMessageException("[DefaultMessageSendService] codecFactory must not be null ,unless eventContent is null");
        }

        byte[] eventContentBytes;
        if (eventContent == null) {
            logger.warn("[DefaultMessageSendService] send message fail! msg={}", "message is null");
            throw new MQSendMessageException("[DefaultMessageSendService] send message fail, message is null");
        }
        eventContentBytes = codecFactory.serialize(eventContent);
        if (eventContentBytes == null) {
            logger.warn("[DefaultMessageSendService] send message fail! msg={}", "serialize result is null");
            throw new MQSendMessageException("[DefaultMessageSendService] send message fail, serialize result is null");
        }
        // 构造成Message
        MqMessage msg = new MqMessage(queueName, exchangeName, eventContentBytes);
        try {
            eventAmqpTemplate.convertAndSend(exchangeName, queueName, msg);
        } catch (AmqpException e) {
            logger.warn("[DefaultMessageSendService] send message fail! msg={}", e.fillInStackTrace());
            throw new MQSendMessageException("[DefaultMessageSendService] send message fail");
        }
    }

    @Override
    public void send(String exchangeName, Object eventContent) throws MQSendMessageException {
        this.send(exchangeName, eventContent, defaultCodecFactory);

    }

    @Override
    public void send(String exchangeName, Object eventContent, ICodecFactory codecFactory) throws MQSendMessageException {
        if (StringUtils.isEmpty(exchangeName)) {
            logger.warn("[DefaultMessageSendService] queueName or exchangeName is empty. exchangeName={}", exchangeName);

            throw new MQSendMessageException("exchangeName can not be empty.");
        }

        if (codecFactory == null) {
            logger.warn("[DefaultMessageSendService] codecFactory must not be null ,unless eventContent is null");
            throw new MQSendMessageException("[DefaultMessageSendService] codecFactory must not be null ,unless eventContent is null");
        }

        byte[] eventContentBytes = null;
        if (eventContent == null) {
            logger.warn("[DefaultMessageSendService] send message fail! msg={}", "message is null");
            throw new MQSendMessageException("[DefaultMessageSendService] send message fail, message is null");
        }
        eventContentBytes = codecFactory.serialize(eventContent);
        if (eventContentBytes == null) {
            logger.warn("[DefaultMessageSendService] send message fail! msg={}", "serialize result is null");
            throw new MQSendMessageException("[DefaultMessageSendService] send message fail, serialize result is null");
        }
        // 构造成Message
        MqMessage msg = new MqMessage(exchangeName, eventContentBytes);
        try {
            eventAmqpTemplate.convertAndSend(exchangeName, null, msg);
        } catch (AmqpException e) {
            logger.error("[DefaultMessageSendService] send message fail! msg={}", e.fillInStackTrace());
            throw new MQSendMessageException("[DefaultMessageSendService] send message fail");
        }
    }
}
