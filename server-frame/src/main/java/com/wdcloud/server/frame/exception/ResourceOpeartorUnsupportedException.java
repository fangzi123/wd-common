package com.wdcloud.server.frame.exception;

import com.wdcloud.utils.exception.BaseException;

public class ResourceOpeartorUnsupportedException extends BaseException {
    public ResourceOpeartorUnsupportedException() {
        super("resource.operator.unsupported");
    }
}
