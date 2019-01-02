package com.wdcloud.utils;

import com.wdcloud.base.exception.BaseException;

import java.security.MessageDigest;

public class MD5Util {

    private static final String IterationCount = "MD5";

    public static String md5(String srcString) {
        if (srcString == null || srcString.length() == 0) {
            throw new BaseException("不能加密空字符串。");
        }

        try {
            MessageDigest md = MessageDigest.getInstance(IterationCount);
            byte[] inputData = srcString.getBytes();
            byte[] md5Bytes = md.digest(inputData);
            StringBuilder hexValue = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("加密错误。");
        }
    }
}
