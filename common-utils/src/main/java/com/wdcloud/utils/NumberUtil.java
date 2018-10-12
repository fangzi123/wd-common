package com.wdcloud.utils;

import java.math.BigDecimal;

/**
 * @author andy
 * @date 2016/12/9.
 */
public class NumberUtil {

    public static int doubleToIntValue(double doubleNum) {
        return Double.valueOf(doubleNum).intValue();
    }

    /**
     * 将字符串表示的元为单位金额转换到分为单位金额
     *
     * @param yuan 金额（单位元）
     * @return 金额（单位分）
     */
    public static int yuan2fen(String yuan) {
        BigDecimal bigDecimal = new BigDecimal(yuan);
        return bigDecimal.multiply(new BigDecimal(100)).intValue();
    }

    /**
     * 转换int表示分为单位的金额到元为单位金额
     *
     * @param fen 金额（单位分）
     * @return 金额（单位元，保留两位小数）
     */
    public static double fen2yuan(int fen) {
        return new BigDecimal(0.01).multiply(BigDecimal.valueOf(fen)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 转换long表示分为单位的金额到元为单位金额
     *
     * @param fen 金额（单位分）
     * @return 金额（单位元，保留两位小数）
     */
    public static double fen2yuan(long fen) {
        return new BigDecimal(0.01).multiply(BigDecimal.valueOf(fen)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
