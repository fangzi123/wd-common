package com.wdcloud.server.frame.api.utils.response;

import com.wdcloud.utils.exception.BaseException;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings(value = "unused")
public enum Code {

    OK(200, "common.success"),
    WARING_MSG(201, "common.waring"),
    TOKEN_TIMEOUT(204, "common.token.timeout"),
    TOKEN_INVALID(205, "common.token.invalid"),

    REDIRECT(301, "common.redirect"),
    ACCOUNT_OTHER_DEVICE_LOGIN(302, "common.other.device.login"),
    ERROR(500, "common.error");


    public final int code;
    public final String name;

    Code(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private final static Map<Integer, Code> valueMap = new HashMap<>();

    static {
        for (Code err : Code.values()) {
            if (valueMap.put(err.code, err) != null) {
                throw new BaseException("Warning! Code 重复定义的值, code=" + err.code);
            }
        }
    }

    public static Code valueOf(int code) {
        Code err = valueMap.get(code);
        if (err != null)
            return err;
        throw new BaseException("不存在code=" + code + "的NPConfigError错误");
    }
}
