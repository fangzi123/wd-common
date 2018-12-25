package com.wdcloud.server.frame.interfaces.info;

/**
 * 联动数据修改消息类
 *
 * @author csf
 * @Date 2015/7/24.
 */
public class LinkedInfo {

    public final String masterId;//操作对象ID
    public final String beanJson;//操作对象ID
    public final String msg; //提醒消息
    public final String[] msgParams; //提醒消息
    public  String userId; //用户ID 联动使用

    public LinkedInfo(String masterId) {
        this(masterId, null);
    }

    public LinkedInfo(String masterId, String beanJson) {
        this(masterId, beanJson, null);
    }

    public LinkedInfo(String masterId, String beanJson, String msg) {
        this(masterId, beanJson, msg, null);
    }

    public LinkedInfo(String masterId, String beanJson, String msg, String... msgParams) {
        this.masterId = masterId;
        this.beanJson = beanJson;
        this.msg = msg;
        this.msgParams = msgParams;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
