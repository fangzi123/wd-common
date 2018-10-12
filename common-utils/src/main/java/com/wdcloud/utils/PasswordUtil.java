package com.wdcloud.utils;

import com.wdcloud.utils.exception.BaseException;

import java.security.MessageDigest;

/**
 * 负责密码哈希的生成和校验，
 * 使用Salted SAH1哈希算法
 */
public class PasswordUtil {

    private static final String IterationCount = "MD5";

    /**
     * 密码加密
     *
     * @param password 密码
     * @return 返回密码的加密值
     */
    public static String haxPassword(String password) {
        if (password == null || password.length() == 0) {
            throw new BaseException("不能加密空密码。");
        }
        try {
            MessageDigest md = MessageDigest.getInstance(IterationCount);
            byte[] inputData = password.getBytes();
            byte[] md5Bytes = md.digest(inputData);
            StringBuilder hexValue = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("加密错误。");
        }
    }

    /**
     * 验证密码
     *
     * @param password    待校验的密码
     * @param haxPassword 加密后的密码
     * @return 密码与哈希是否匹配
     */
    public static boolean verify(String password, String haxPassword) {
        if (password == null || password.length() == 0) {
            throw new BaseException("不能验证空密码。");
        }
        if (haxPassword == null || haxPassword.length() == 0) {
            throw new BaseException("加密后的密码不能为空。");
        }
        return haxPassword(password).equals(haxPassword);
    }

    public static void main(String[] args){
        System.out.println(haxPassword("123qweasD"));
    }
}