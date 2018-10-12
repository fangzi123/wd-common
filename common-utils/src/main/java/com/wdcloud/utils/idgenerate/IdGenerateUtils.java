package com.wdcloud.utils.idgenerate;

import com.wdcloud.utils.RandomUtils;
import com.wdcloud.utils.StringUtil;
import com.wdcloud.utils.exception.BaseException;

/**
 * ID 生成器
 *
 * @auther csf
 * @date 2015/12/21.
 */

public class IdGenerateUtils {

    public static final long TIMESTAMP = 1450664787504L; //2015-12-21 10:28:28
    public static final long TIMESTAMP_LEFT_SHIFT = 22L; //时间左移位数 时间戳占42位

    public static final long TYPE_RANDOM_BITS = 16; //用户ID占用长度
    public static final long MAX_RANDOM_NO = ~(-1L << TYPE_RANDOM_BITS); //最大支持编号类别数0~65535
    public static final long TYPE_RANDOM_SHIFT = 6L; //编号类别左移位数
    public static final long MAX_SEQUENCE = ~(-1L << 6); //最大支服务数0~63


    private static long lastTimestamp = -1L;
    private static long sequence = 0L; //序列号

    private IdGenerateUtils() {

    }

    /**
     * 生成ID
     *
     * @param prefix 前缀可为空，当为空时，返回为Long类型
     * @param seed 随机数生成因子
     * @return ID
     */
    public synchronized static String getId(String prefix, int seed) {

        int random = RandomUtils.randomNum(seed);
        if (random > MAX_RANDOM_NO || random < 0) {
            throw new BaseException(String.format("serverNo userId/10000 can't be greater than %d or less than 0", MAX_RANDOM_NO));
        }

        long time = timeGen(); //获取当前毫秒数
        //如果服务器时间有问题(时钟后退) 报错。
        if (time < lastTimestamp) {
            throw new BaseException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - TIMESTAMP));
        }

        if (lastTimestamp == time) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //判断是否溢出,也就是每毫秒内超过1024，当为1024时，与maxSequence相与，sequence就等于0
            if (sequence == 0) {
                time = tilNextMillis(lastTimestamp); //自旋等待到下一毫秒
            }
        } else {
            sequence = 0L; //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
        }
        lastTimestamp = time;
        if (StringUtil.isEmpty(prefix)) {
            return ((time - TIMESTAMP << TIMESTAMP_LEFT_SHIFT) | (random << TYPE_RANDOM_SHIFT) | sequence) + "";
        } else {
            // 最后按照规则拼出ID。
            // 000000000000000000000000000000000000000000                        65535                       000000
            // time                                                              random                      sequence
            return prefix + "" + ((time - TIMESTAMP << TIMESTAMP_LEFT_SHIFT) | (random << TYPE_RANDOM_SHIFT) | sequence);
        }
    }

    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
//            timestamp = tilNextMillis(lastTimestamp);
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected static long timeGen() {
        return System.currentTimeMillis();
    }
}
