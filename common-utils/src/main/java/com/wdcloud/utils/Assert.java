package com.wdcloud.utils;

import com.wdcloud.utils.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 判断是否为空
 *
 * @author csf
 * @date 2016/6/13.
 */
@SuppressWarnings(value = "unused")
public class Assert {
    private static Logger logger = LoggerFactory.getLogger(Assert.class);

    public static void notNull(Object object, String message) {
        if (object == null) {
            logger.error(message);
            throw new BaseException(message);
        }
    }

    public static void notNull(String str, String message) {
        if (str == null || "".equals(str.trim())) {
            logger.error(message);
            throw new BaseException(message);
        }
    }

    public static void notEmpty(List list, String message) {
        if (list == null || list.isEmpty()) {
            logger.error(message);
            throw new BaseException(message);
        }
    }

    public static void notEmpty(Map map, String message) {
        if (map == null || map.isEmpty()) {
            logger.error(message);
            throw new BaseException(message);
        }
    }
}
