package com.wdcloud.mq.core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 消息体
 *
 * @author Andy
 * @date 17/4/17
 */
@SuppressWarnings("unused")
public class MqMessage implements Serializable {

    private String queueName;

    private String exchangeName;

    private byte[] msgBytes;

    public MqMessage(String exchangeName, byte[] msgBytes) {
        this(null, exchangeName, msgBytes);
    }

    public MqMessage(String queueName, String exchangeName, byte[] msgBytes) {
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.msgBytes = msgBytes;
    }

    public MqMessage() {
    }

    public String getQueueName() {
        return queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public byte[] getMsgBytes() {
        return msgBytes;
    }

    @Override
    public String toString() {
        return "EopEventMessage [queueName=" + queueName + ", exchangeName="
                + exchangeName + ", msgBytes=" + Arrays.toString(msgBytes)
                + "]";
    }
}
