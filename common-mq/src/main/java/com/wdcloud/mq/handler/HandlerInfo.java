package com.wdcloud.mq.handler;

/**
 * @author Andy
 * @date 17/10/17
 */
public class HandlerInfo {

    private MQHandler handler;

    private IMqMessageHandler mqMessageHandler;

    public HandlerInfo(MQHandler handler, IMqMessageHandler mqMessageHandler) {
        this.handler = handler;
        this.mqMessageHandler = mqMessageHandler;
    }

    public MQHandler getHandler() {
        return handler;
    }

    public IMqMessageHandler getMqMessageHandler() {
        return mqMessageHandler;
    }

    public void setHandler(MQHandler handler) {
        this.handler = handler;
    }

    public void setMqMessageHandler(IMqMessageHandler mqMessageHandler) {
        this.mqMessageHandler = mqMessageHandler;
    }
}
