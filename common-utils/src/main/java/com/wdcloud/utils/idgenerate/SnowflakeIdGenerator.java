package com.wdcloud.utils.idgenerate;

import java.util.Calendar;

/**
 * Twitter_Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 最高位符号位，固定为0<br>
 * 41位时间戳(毫秒级)，注意，41位时间截不是存储当前时间的时间戳，而是存储时间戳的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间戳，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
 * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 *
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，
 * 并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
public class SnowflakeIdGenerator {

    // 时间戳起始时间
    private final static long startTimestamp = new Calendar.Builder()
            .setDate(2015, 0, 01)
            .build()
            .getTimeInMillis();

    private final static long workerIdBits = 5L;
    private final static long datacenterBits = 5L;

    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterBits);

    private final static long sequenceBits = 12L;

    private final static long workerIdShift = sequenceBits;
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    private final static long timestampShift = sequenceBits + workerIdBits + datacenterBits;

    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

//    // 工作机器ID (0~31)
//    private final long workerId;
//    // 数据中心ID(0~31)
//    private final long datacenterId;

    private final long datacenterWorkerIdValue;

    private long lastTimestamp = -1;
    private long sequence = 0;

    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("Datacenter ID can't be greater than %d or less than 0", maxDatacenterId));
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", maxWorkerId));
        }
//        this.datacenterId = datacenterId;
//        this.workerId = workerId;

        datacenterWorkerIdValue = (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift);
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - startTimestamp) << timestampShift)
                | datacenterWorkerIdValue
                | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.out.println(startTimestamp);
    }
}
