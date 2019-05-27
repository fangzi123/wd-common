package com.wdcloud.base.exception;

import com.wdcloud.base.exception.BaseException;

public class DuplicateNameException extends BaseException{
    private static final long serialVersionUID = 5096375539093102240L;

    public DuplicateNameException() {
        super();
    }

    public DuplicateNameException(String message) {
        super(message);
    }
}
