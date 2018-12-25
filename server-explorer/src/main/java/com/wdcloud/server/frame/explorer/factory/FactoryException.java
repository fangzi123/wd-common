package com.wdcloud.server.frame.explorer.factory;

import com.wdcloud.base.exception.BaseException;

/**
 * 工厂异常
 *
 * @author csf
 * @Date 2015/7/24.
 */
public class FactoryException extends BaseException {
    /**
     * 指定消息
     *
     * @param message 异常内容
     */
    public FactoryException(String message) {
        super(message);
    }

    /**
     * 指定消息
     *
     * @param message 异常内容
     */
    public FactoryException(String message, String... params) {
        super(message, params);
    }

    /**
     * 指定消息&异常
     *
     * @param message 异常内容
     * @param cause   异常
     */
    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
