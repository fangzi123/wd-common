package com.wdcloud.server.frame.api.utils.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wdcloud.server.frame.MessageUtil;
import com.wdcloud.utils.StringUtil;

import java.io.Serializable;
import java.text.MessageFormat;

public class Response<T> implements Serializable {

    private static final long serialVersionUID = 586996702847312618L;

    private int code;
    private String message;
    private T entity;

    public Response(Code code, T entity) {
        this.code = code.code;
        this.entity = entity;
    }

    public Response(Code code, String msg, String[] params) {
        this(code, null, msg, params);
    }

    public Response(Code code, T entity, String msg) {
        this.code = code.code;
        this.entity = entity;
//        LocaleMessageSourceBean bean = DynamicApplicationContext.getBean(LocaleMessageSourceBean.class);

        this.message = MessageUtil.getMessage(msg);
        if (StringUtil.isEmpty(message)) {
            this.message = msg;
        }
    }

    private String getTranslateMsg(String msg) {
        if (StringUtil.isNotEmpty(msg)) {
//            LocaleMessageSourceBean bean = DynamicApplicationContext.getBean(LocaleMessageSourceBean.class);
            String message = MessageUtil.getMessage(msg);
            if (StringUtil.isNotEmpty(message)) {
                return message;
            }
        }
        return null;
    }

    public Response(Code code, T entity, String msg, String[] params) {
        this.code = code.code;

        if (code != Code.OK && StringUtil.isNotEmpty(msg)) {
//            LocaleMessageSourceBean bean = DynamicApplicationContext.getBean(LocaleMessageSourceBean.class);

            String message = getTranslateMsg(msg);
            if (StringUtil.isEmpty(message)) {
                this.message = msg;
            } else if (params != null && params.length > 0) {
                String[] arrays = new String[params.length];
                int index = 0;
                for (String param : params) {
                    String translate = MessageUtil.getMessage(param);
                    if (StringUtil.isEmpty(translate)) {
                        arrays[index] = param;
                    } else {
                        arrays[index] = MessageUtil.getMessage(param);
                    }
                    index++;
                }
                this.message = MessageFormat.format(message, arrays);
            } else {
                this.message = message;
            }

        }
        this.entity = entity;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public static <T> String returnResponse(Code code, T entity, String msg, String[] params) {
        return JSON.toJSONString(new Response<>(code, entity, msg, params), SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> String returnResponse(Code code, T entity, String msg) {
        return JSON.toJSONString(new Response<>(code, entity, msg), SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> String returnResponse(Code code, String msg) {
        return JSON.toJSONString(new Response<>(code, msg), SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> String returnDateFormatResponse(Code code, T entity, String dataFormat, String message, String[] params) {
        if (StringUtil.isEmpty(dataFormat)) {
            dataFormat = "yyyy-MM-dd HH:mm:ss";
        }
        return JSON.toJSONStringWithDateFormat(new Response<>(code, entity, message, params), dataFormat,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    public static <T> String returnDateFormatResponse(Code code, T entity, String message, String[] params) {
        String dataFormat = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONStringWithDateFormat(new Response<>(code, entity, message, params), dataFormat,
                SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String returnResponse(Code code, String message, String[] params) {
        return JSON.toJSONString(new Response<>(code, message, params), SerializerFeature.DisableCircularReferenceDetect);
    }
}