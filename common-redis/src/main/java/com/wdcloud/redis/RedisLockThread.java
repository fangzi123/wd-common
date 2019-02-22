package com.wdcloud.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andy
 * @date 2016/10/10.
 */
public class RedisLockThread {
    /**
     * 当前jvm内持有该锁的线程
     */
    private static Map<Long, Thread> threadMap = new HashMap<Long, Thread>();

    public static void setThreadMap(long threadId, Thread thread) {
        threadMap.put(threadId, thread);
    }

    public static Thread getThread(long threadId) {
        return threadMap.get(threadId);
    }

    public static void removeThread(long threadId) {
        threadMap.remove(threadId);
    }
}
