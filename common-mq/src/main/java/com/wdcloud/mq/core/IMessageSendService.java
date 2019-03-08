package com.wdcloud.mq.core;


import com.wdcloud.mq.codec.ICodecFactory;
import com.wdcloud.mq.exception.MQSendMessageException;

/**
 * 消息发送接口
 *
 * @author Andy
 * @date 17/4/17
 */
public interface IMessageSendService {

    /**
     * 消息发送
     *
     * @param queueName    消息名称
     * @param exchangeName 交换名称
     * @param eventContent 消息休
     * @throws MQSendMessageException
     */
    void send(String queueName, String exchangeName, Object eventContent) throws MQSendMessageException;

    /**
     * 消息发送
     *
     * @param queueName    消息名称
     * @param exchangeName 交换名称
     * @param eventContent 消息休
     * @param codecFactory 序列化实现
     * @throws MQSendMessageException
     */
    void send(String queueName, String exchangeName, Object eventContent, ICodecFactory codecFactory) throws MQSendMessageException;

    /**
     * 消息发送（广播）
     *
     * @param exchangeName 交换名称
     * @param eventContent 消息休
     * @throws MQSendMessageException
     */
    void send(String exchangeName, Object eventContent) throws MQSendMessageException;

    /**
     * 消息发送（广播）
     *
     * @param exchangeName 交换名称
     * @param eventContent 消息休
     * @param codecFactory 序列化实现
     * @throws MQSendMessageException
     */
    void send(String exchangeName, Object eventContent, ICodecFactory codecFactory) throws MQSendMessageException;
}
