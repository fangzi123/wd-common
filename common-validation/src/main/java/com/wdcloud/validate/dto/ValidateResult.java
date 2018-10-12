package com.wdcloud.validate.dto;

import java.io.Serializable;

public class ValidateResult implements Serializable {

    private static final long serialVersionUID = -2063730025447145835L;

    private String msgKey;
    private String msgBody;

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }
}