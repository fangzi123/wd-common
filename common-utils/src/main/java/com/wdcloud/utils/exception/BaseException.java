package com.wdcloud.utils.exception;

/**
 * @author andy
 * @date 2016/6/13.
 */
public class BaseException extends RuntimeException {

    protected String[] i18nMsg;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, String... i18nMsg) {
        super(message);
        this.i18nMsg = i18nMsg;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public String[] getI18nMsg() {
        return i18nMsg;
    }

    public void setI18nMsg(String... i18nMsg) {
        this.i18nMsg = i18nMsg;
    }
}
