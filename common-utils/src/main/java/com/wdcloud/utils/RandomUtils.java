package com.wdcloud.utils;

import java.util.Random;

public class RandomUtils {

    private static final int DEFAULT_SEED = 65535;

    /**
     * 生成随机数(全部为正数)
     *
     * @param count 生成随机数位数
     * @return 返回随机数
     */
    public static String randomAuthCode(int count) {
        assert count > 0;
        return randomAuthCode(count, DEFAULT_SEED);
    }

    /**
     * 生成随机数 (全部为正数)
     *
     * @param count 生成随机数位数
     * @param bound 边界
     * @return 返回随机数
     */
    public static String randomAuthCode(int count, Integer bound) {
        assert count > 0;
        StringBuilder result = new StringBuilder();
        if (bound == null) {
            bound = DEFAULT_SEED;
        }
        for (int i = 0; i < count; i++) {
            result.append(Math.abs(randomNum(bound)));
        }
        return result.toString();
    }

    /**
     * 获取随机数
     *
     * @param bound 边界
     * @return 返回随机数
     */
    public static int randomNum(Integer bound) {
        if (bound == null) {
            bound = DEFAULT_SEED;
        }
        Random random = new Random();
        return Math.abs(random.nextInt(bound));
    }

    /**
     * 生成字符和数据的随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(62)));
        }
        return sb.toString();
    }
}