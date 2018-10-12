package com.wdcloud.validate.exceptions;

import lombok.Data;

@Data
public class ValidateException extends RuntimeException {

    protected String[] i18nParam;

    public ValidateException() {
        super();
    }

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, String... i18nParam) {
        super(message);
        this.i18nParam = i18nParam;
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);

    }
    public ValidateException(String message, Throwable cause, String... i18nParam) {
        super(message, cause);
        this.i18nParam = i18nParam;
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }
}
