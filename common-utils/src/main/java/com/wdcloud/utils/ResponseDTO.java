package com.wdcloud.utils;

import java.io.Serializable;

public class ResponseDTO<T> implements Serializable {

    private boolean success;
    private String message;
    private T entity;


    public ResponseDTO() {
        this.success = true;
    }

    public ResponseDTO(boolean status) {
        this.success = status;
    }

    public ResponseDTO(boolean status, T entity) {
        this.success = status;
        this.entity = entity;
    }

    public ResponseDTO(boolean status, String message, T entity) {
        this.success = status;
        this.message = message;
        this.entity = entity;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }


    /**
     * 当需要返回一个OK状态时，可以使用本接口
     */
    public static ResponseDTO success() {
        return new ResponseDTO<>();
    }

    /**
     * 当需要返回一个OK状态时，可以使用本接口
     *
     * @param entity 成功时，可带回一个对象
     */
    public static <T> ResponseDTO<T> success(T entity) {
        return new ResponseDTO<>(true, entity);
    }

    /**
     * 当需要返回一个OK状态时，可以使用本接口
     *
     * @param entity  成功时，将实体带回
     * @param message 成功时，可附件一段回显信息
     */
    public static <T> ResponseDTO<T> OK(T entity, String message) {
        return new ResponseDTO<>(true, message, entity);
    }

    /**
     * 当需要返回一个notOK状态时，可以使用本接口
     */
    public static ResponseDTO notOK() {
        return new ResponseDTO(false);
    }

    /**
     * 当需要返回一个notOK状态时，可以使用本接口
     *
     * @param err 错误信息
     */
    public static ResponseDTO notOK(String err) {
        return new ResponseDTO<>(false, err);
    }

    /**
     * 当需要返回一个notOK状态时，可以使用本接口
     *
     * @param entity 失败时，可带回一个对象，多数情况下带回null
     */
    public static <T> ResponseDTO<T> notOK(T entity) {
        return new ResponseDTO<>(false, entity);
    }

    /**
     * 当需要返回一个notOK状态时，可以使用本接口
     *
     * @param entity 失败时，可带回一个对象，多数情况下带回null
     */
    public static <T> ResponseDTO<T> notOK(T entity, String err) {
        return new ResponseDTO<>(false, err, entity);
    }
}