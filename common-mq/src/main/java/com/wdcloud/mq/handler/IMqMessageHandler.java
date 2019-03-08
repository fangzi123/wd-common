package com.wdcloud.mq.handler;

/**
 * 消息接收及解析
 *
 * @author Andy
 * @date 17/4/17
 */
public interface IMqMessageHandler {
    void processMsg(Object e);
}
