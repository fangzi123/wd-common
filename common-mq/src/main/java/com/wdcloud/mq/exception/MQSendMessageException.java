package com.wdcloud.mq.exception;

/**
 * 消息队列发送消息异常
 *
 * @author Andy
 * @date 17/4/17
 */
@SuppressWarnings("serial")
public class MQSendMessageException extends RuntimeException {

    public MQSendMessageException() {
        super();
    }

    public MQSendMessageException(String param, Throwable e) {
        super(param, e);
    }

    public MQSendMessageException(String param) {
        super(param);
    }

    public MQSendMessageException(Throwable e) {
        super(e);
    }

}
