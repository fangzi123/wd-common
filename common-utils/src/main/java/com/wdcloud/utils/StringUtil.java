package com.wdcloud.utils;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings(value = "unused")
public class StringUtil {
    private static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 过滤空串
     *
     * @param str 字符串
     * @return 返回结果
     */
    public static String filterNull(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * 两个字符串是否相等
     *
     * @param source 原串
     * @param target 目标串
     * @return 结果
     */
    public static boolean stringEquals(String source, String target) {
        return isEmpty(source) && isEmpty(target) || !(isEmpty(source) || isEmpty(target)) && source.equals(target);
    }

    public static boolean isEmpty(String str) {
        return filterNull(str).equals("");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean containsAny(String str, String... flag) {
        if (str != null) {
            if (flag == null || flag.length == 0) {
                flag = "[-{-}-]-,".split("-");
            }
            for (String s : flag) {
                if (str.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否是 JSON
     *
     * @param json 　字符串
     * @return 　返回是否是正确JSON
     */
    public static boolean isGoodJson(String json) {
        if (isNotEmpty(json)) {
            try {
                JSON.parse(json);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否是 对象 JSON
     *
     * @param json 　字符串
     * @return 　返回是否是正确JSON
     */
    public static boolean isGoodObjectJson(String json) {
        if (isNotEmpty(json)) {
            try {
                JSON.parseObject(json);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否是 数组 JSON
     *
     * @param json 　字符串
     * @return 　返回是否是正确JSON
     */
    public static boolean isGoodArrayJson(String json) {
        if (isNotEmpty(json)) {
            try {
                JSON.parseArray(json);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String[] listToStringArray(List<String> list) {
        if (list != null && !list.isEmpty()) {
            return list.toArray(new String[list.size()]);
        }
        return new String[0];
    }

    public static String[] setToStringArray(Set<String> set) {
        if (set != null && !set.isEmpty()) {
            return set.toArray(new String[set.size()]);
        }
        return new String[0];
    }

    public static List<String> stringArrayToList(String... strings) {
        if (strings != null && strings.length > 0) {
            return Arrays.asList(strings);
        }
        return new ArrayList<>();
    }


    /**
     * MD5签名算法
     *
     * @param res
     * @return
     */
    public static String md5Digest(String res) {
        if (res == null || "".equals(res)) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] strTemp;
        try {
            strTemp = res.getBytes("gbk");
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}