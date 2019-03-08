package com.wdcloud.mq.core;

import com.wdcloud.mq.codec.ICodecFactory;
import com.wdcloud.mq.handler.IMqMessageHandler;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;


/**
 * 消息队列事件控制器
 *
 * @author Andy
 * @date 17/4/17
 */
public interface IEventController {

    /**
     * 控制器启动方法 默认使用 TopicExchange
     */
    void start();

    /**
     * 控制器启动方法
     *
     * @param exchange 目前支持（TopicExchange，DirectExchange）
     */
    void start(Exchange... exchange);

    /**
     * 获取发送模版
     */
    IMessageSendService getEopEventTemplate();

    /**
     * 获取临时队列，广播时用
     */
    Queue getDeclareQueue();

    /**
     * 绑定消费程序到对应的exchange和queue
     *
     * @param queueName      队列名称
     * @param exchangeName   交换区名称
     * @param messageHandler 消息处理器
     * @return
     */
    IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler);

    /**
     * 绑定消费程序到对应的exchange和queue
     *
     * @param queueName      队列名
     * @param exchangeName   交换区名称
     * @param messageHandler 消费端 
     * @param codecFactory   编解码器
     * @return
     */
    IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler, ICodecFactory codecFactory);

    /**
     * 绑定消费程序到对应的exchange和queue
     *
     * @param queueName      队列名称
     * @param exchangeName   交换区名称
     * @param messageHandler 消息处理器
     * @param isFanout       是不是广播
     * @return
     */
    IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler, boolean isFanout);

    /**
     * 绑定消费程序到对应的exchange和queue
     *
     * @param queueName      队列名
     * @param exchangeName   交换区名称
     * @param messageHandler 消费端 
     * @param codecFactory   编解码器
     * @param isFanout       是不是广播
     * @return
     */
    IEventController add(String queueName, String exchangeName, IMqMessageHandler messageHandler, ICodecFactory codecFactory, boolean isFanout);
}
