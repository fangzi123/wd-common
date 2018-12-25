package com.wdcloud.server.frame.exception;

import com.wdcloud.base.exception.BaseException;

public class SystemErrorException extends BaseException {
    public SystemErrorException() {
        super("common.error");
    }
}
