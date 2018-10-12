package com.wdcloud.server.frame.exception;

import com.wdcloud.utils.exception.BaseException;

public class ParamErrorException extends BaseException {
    public ParamErrorException() {
        super("params.error");
    }
}
