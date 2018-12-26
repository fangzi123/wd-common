package com.wdcloud.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author 赵秀非
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedisServiceTestApplication.class})
public class RedisServiceTest {

    @Autowired
    private IRedisService redisService;

    private static String KEY = "key";
    private static String VALUE = "value";
    private static String KEY1 = KEY + 1;
    private static String VALUE1 = VALUE + 1;
    private static String KEY2 = KEY + 2;
    private static String VALUE2 = VALUE + 2;
    private static long TIMEOUT_SECONDS = 3L;
    private static long TIMEOUT_MILLISECONDS = 3L * 1000;

    private static Map<String, String> MAPS = Map.of(KEY, VALUE, KEY1, VALUE1, KEY2, VALUE2);

    public void delAll() {
        redisService.del(KEY);
        redisService.del(KEY1);
        redisService.del(KEY2);
    }

    @Test
    public void redisTemplate() {
        delAll();
        RedisTemplate<String, String> redisTemplate = redisService.redisTemplate();
        Assert.assertNotNull(redisTemplate);
    }

    @Test
    public void set() {
        delAll();
        redisService.set(KEY, VALUE);
        String value = redisService.get(KEY);
        assertEquals(VALUE, value);

    }

    @Test
    public void get() {
        delAll();
        redisService.set(KEY, VALUE);
        String value = redisService.get(KEY);
        assertEquals(VALUE, value);
    }

    @Test
    public void getRange() {
        delAll();
    }

    @Test
    public void getSet() {
        delAll();
        redisService.set(KEY, VALUE);
        String value = redisService.getSet(KEY, VALUE1);
        assertEquals(VALUE, value);
    }

    @Test
    public void mGet() {
        delAll();
        redisService.mSet(MAPS);
        List list = redisService.mGet(List.of(KEY, KEY1));
        assertEquals(2, list.size());
    }

    @Test
    public void setEx() {
        delAll();
        redisService.setEx(KEY, VALUE, TIMEOUT_SECONDS);
        String value = redisService.get(KEY);
        Assert.assertNotNull(value);
        sleep();
        value = redisService.get(KEY);
        assertNull(value);
    }

    private void sleep() {
        try {
            Thread.sleep(TIMEOUT_MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setNx() {
        delAll();
        redisService.setNx(KEY, VALUE);
        redisService.setNx(KEY, VALUE1);
        String o = redisService.get(KEY);
        assertEquals(VALUE, o);
    }

    @Test
    public void setRange() {
        delAll();
        redisService.set(KEY, "value");
        redisService.setRange(KEY, 1, "b");
        String o = redisService.get(KEY);
        assertEquals("vblue", o);
    }

    @Test
    public void strLen() {
        delAll();
        redisService.set(KEY, VALUE);
        long len = redisService.strLen(KEY);
        assertEquals(5, len);
    }

    @Test
    public void mSet() {
        delAll();
        redisService.mSet(MAPS);
        String value = redisService.get(KEY);
        assertEquals(VALUE, value);
    }

    @Test
    public void mSetNx() {
        delAll();
        Boolean b = redisService.mSetNx(MAPS);
        assertTrue(b);
    }

    @Test
    public void pSetEx() {
        redisService.pSetEx(KEY, VALUE, TIMEOUT_MILLISECONDS);
        assertNotNull(redisService.get(KEY));
        sleep();
        assertNull(redisService.get(KEY));
    }

    @Test
    public void incr() {
        delAll();
        redisService.set(KEY, 1 + "");
        assertEquals(2, redisService.incr(KEY).longValue());
    }

    @Test
    public void incrBy() {
        delAll();
        redisService.set(KEY, 1 + "");
        assertEquals(2, redisService.incrBy(KEY, -1).longValue());
        assertEquals(3, redisService.incrBy(KEY, 1).longValue());
    }

    @Test
    public void incrByFloat() {
        delAll();
        redisService.set(KEY, 1 + "");
        assertEquals(2.25, redisService.incrByFloat(KEY, -1.25).longValue(), 1.25);
        assertEquals(3.25, redisService.incrByFloat(KEY, 1).longValue(), 1);
    }

    @Test
    public void decr() {
        delAll();
        redisService.set(KEY, 1 + "");
        assertEquals(0, redisService.decr(KEY).longValue());
        assertEquals(-1, redisService.decr(KEY).longValue());
    }

    @Test
    public void decrBy() {
        delAll();
        redisService.set(KEY, 1 + "");
        assertEquals(0, redisService.decrBy(KEY, -1).longValue());
        assertEquals(-1, redisService.decrBy(KEY, 1).longValue());
    }

    @Test
    public void decrByFloat() {
        delAll();
        redisService.set(KEY, 1 + "");
        assertEquals(-0.25, redisService.decrByFloat(KEY, -1.25).longValue(), 1.25);
        assertEquals(-1.25, redisService.decrByFloat(KEY, 1).longValue(), 1);
    }

    @Test
    public void append() {
        delAll();
        redisService.set(KEY, VALUE);
        assertEquals(10, redisService.append(KEY, VALUE).intValue());
    }

    @Test
    public void hDel() {
        delAll();
        redisService.hSet(KEY, KEY1, VALUE1);
        redisService.hSet(KEY, KEY2, VALUE2);
        redisService.hDel(KEY, KEY1);
        assertEquals(1, redisService.hGetAll(KEY).values().size());
    }

    @Test
    public void hExists() {
        delAll();
        redisService.hSet(KEY, KEY1, VALUE1);
        assertTrue(redisService.hExists(KEY, KEY1));
    }

    @Test
    public void hGet() {
        delAll();
        redisService.hSet(KEY, KEY1, VALUE1);
        assertNotNull(redisService.hGet(KEY, KEY1));
    }

    @Test
    public void hGetAll() {
        delAll();
        redisService.hSet(KEY, KEY1, VALUE1);
        redisService.hSet(KEY, KEY2, VALUE2);
        assertEquals(2, redisService.hGetAll(KEY).values().size());
    }

    @Test
    public void hIncr() {
        delAll();
        redisService.hSet(KEY, KEY1, 1 + "");
        assertEquals(2, redisService.hIncr(KEY, KEY1).longValue());
    }

    @Test
    public void hIncrBy() {
        delAll();
        redisService.hSet(KEY, KEY1, 1 + "");
        assertEquals(4, redisService.hIncrBy(KEY, KEY1, 3).longValue());
        assertEquals(7, redisService.hIncrBy(KEY, KEY1, -3).longValue());
    }

    @Test
    public void hIncrByFloat() {
        delAll();
        redisService.hSet(KEY, KEY1, 1.2 + "");
        assertEquals(4.2, redisService.hIncrByFloat(KEY, KEY1, 3).longValue(), 3);
        assertEquals(7.2, redisService.hIncrByFloat(KEY, KEY1, -3).longValue(), 3);
    }

    @Test
    public void hDecr() {
        delAll();
        redisService.hSet(KEY, KEY1, 1 + "");
        assertEquals(0, redisService.hDecr(KEY, KEY1).longValue());
    }

    @Test
    public void hDecrBy() {
        delAll();
        redisService.hSet(KEY, KEY1, 1 + "");
        assertEquals(-1, redisService.hDecrBy(KEY, KEY1, 2).longValue());
    }

    @Test
    public void hDecrByFloat() {
        delAll();
        redisService.hSet(KEY, KEY1, 1 + "");
        assertEquals(-1.3, redisService.hDecrByFloat(KEY, KEY1, 2.3).longValue(), 1.3);
    }

    @Test
    public void hKeys() {
        delAll();
        redisService.hSet(KEY, KEY1, VALUE);
        assertEquals(1, redisService.hKeys(KEY).size());
    }

    @Test
    public void hLen() {
        delAll();
        redisService.hSet(KEY, KEY1, VALUE);
        assertEquals(1, redisService.hLen(KEY).longValue());

    }

    @Test
    public void hMget() {
        delAll();
        redisService.hMset(KEY, MAPS);
        assertEquals(2, redisService.hMget(KEY, List.of(KEY, KEY1)).size());
    }

    @Test
    public void hMset() {
        delAll();
        redisService.hMset(KEY, MAPS);
        assertEquals(2, redisService.hMget(KEY, List.of(KEY, KEY1)).size());
    }

    @Test
    public void hSet() {
        delAll();
        redisService.hSet(KEY, KEY1, VALUE1);
        assertEquals(VALUE1, redisService.hGet(KEY, KEY1));
    }

    @Test
    public void hSetNx() {
        delAll();
        redisService.hSetNx(KEY, KEY1, VALUE1);
        redisService.hSetNx(KEY, KEY1, VALUE2);
        assertEquals(VALUE1, redisService.hGet(KEY, KEY1));
    }

    @Test
    public void hVals() {
        delAll();
        redisService.hMset(KEY, MAPS);
        List<String> obs = redisService.hVals(KEY);
        assertEquals(3, obs.size());
    }

    @Test
    public void hScan() {
        delAll();
    }

    @Test
    public void bLpop() {
        delAll();
        String str = redisService.bLpop(KEY, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNull(str);
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        str = redisService.bLpop(KEY, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(VALUE1, str);
    }

    @Test
    public void rLpop() {
        delAll();
        String str = redisService.rLpop(KEY, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNull(str);
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        str = redisService.rLpop(KEY, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(VALUE, str);
    }

    @Test
    public void bRpopLpush() {
        delAll();
        String str = redisService.bRpopLpush(KEY, KEY1, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertNull(str);
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        str = redisService.bRpopLpush(KEY, KEY1, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        assertEquals(VALUE, str);
    }

    @Test
    public void lInsert() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        redisService.lInsert(KEY, VALUE, VALUE2);
        assertEquals(VALUE2, redisService.lIndex(KEY, 1));

    }

    @Test
    public void lLen() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        assertEquals(2, redisService.lLen(KEY).longValue());
    }

    @Test
    public void lIndex() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        assertEquals(VALUE1, redisService.lIndex(KEY, 0));
    }

    @Test
    public void lPop() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        redisService.lPush(KEY, VALUE2);
        assertEquals(VALUE2, redisService.lPop(KEY));
    }

    @Test
    public void lPush() {
        delAll();
        redisService.lPush(KEY, VALUE);
        assertEquals(VALUE, redisService.lPop(KEY));
    }

    @Test
    public void lPushX() {
        delAll();
        assertEquals(0, redisService.lPushX(KEY, VALUE).longValue());
        redisService.lPush(KEY, VALUE);
        assertEquals(2, redisService.lPushX(KEY, VALUE1).longValue());
    }

    @Test
    public void lRange() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        redisService.lPush(KEY, VALUE2);
        redisService.lPush(KEY, VALUE + 3);
        redisService.lPush(KEY, VALUE + 4);
        redisService.lPush(KEY, VALUE + 5);
        List<String> los = redisService.lRange(KEY, 1, 2);
        assertEquals(los, List.of(VALUE + 4, VALUE + 3));
    }

    @Test
    public void lRem() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        redisService.lPush(KEY, VALUE1);
        redisService.lPush(KEY, VALUE2);
        assertEquals(2, redisService.lRem(KEY, 2, VALUE1).longValue());
    }

    @Test
    public void lSet() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        redisService.lPush(KEY, VALUE2);
        redisService.lPush(KEY, VALUE + 3);
        redisService.lPush(KEY, VALUE + 4);
        redisService.lPush(KEY, VALUE + 5);
        redisService.lSet(KEY, 2, "HELLO WORLD!");
        String str = redisService.lIndex(KEY, 2);
        assertEquals("HELLO WORLD!", str);
    }

    @Test
    public void lTrim() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        redisService.lPush(KEY, VALUE2);
        redisService.lPush(KEY, VALUE + 3);
        redisService.lPush(KEY, VALUE + 4);
        redisService.lPush(KEY, VALUE + 5);
        redisService.lTrim(KEY, 1, 2);
        List<String> los = redisService.lRange(KEY, 0, -1);
        assertEquals(los, List.of(VALUE + 4, VALUE + 3));

    }

    @Test
    public void rPop() {
        delAll();
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        redisService.lPush(KEY, VALUE2);
        assertEquals(VALUE, redisService.rPop(KEY));
    }

    @Test
    public void rPopLpush() {
        delAll();
        redisService.bRpopLpush(KEY, KEY1, TIMEOUT_SECONDS, TimeUnit.SECONDS);
        redisService.lPush(KEY, VALUE);
        redisService.lPush(KEY, VALUE1);
        assertEquals(VALUE, redisService.bRpopLpush(KEY, KEY1, TIMEOUT_SECONDS, TimeUnit.SECONDS));
    }

    @Test
    public void rPush() {
        delAll();
        redisService.rPush(KEY, VALUE);
        redisService.rPush(KEY, VALUE1);
        redisService.rPush(KEY, VALUE2);
        assertEquals(VALUE, redisService.lPop(KEY));
    }

    @Test
    public void rPushX() {
        delAll();
        assertEquals(0, redisService.rPushX(KEY, VALUE).longValue());
        redisService.lPush(KEY, VALUE);
        assertEquals(2, redisService.rPushX(KEY, VALUE1).longValue());
    }

    @Test
    public void sAdd() {
        delAll();
        redisService.sAdd(KEY, VALUE);
        assertTrue(redisService.sIsMember(KEY, VALUE));
        delAll();
        redisService.sAdd(KEY, new String[]{VALUE, VALUE, VALUE1, VALUE2});
        assertEquals(3, redisService.sCard(KEY).longValue());
        redisService.sAdd(KEY, List.of(VALUE, VALUE, VALUE1, VALUE2));
        assertEquals(3, redisService.sCard(KEY).longValue());
    }


    @Test
    public void sCard() {
        delAll();
        redisService.sAdd(KEY, new String[]{VALUE, VALUE, VALUE1, VALUE2});
        assertEquals(3, redisService.sCard(KEY).longValue());

    }

    @Test
    public void sDiff() {
        delAll();
        redisService.sAdd(KEY, List.of(VALUE, VALUE, VALUE1, VALUE2));
        redisService.sAdd(KEY1, List.of(VALUE1, VALUE2));
        assertEquals(1, redisService.sDiff(KEY, KEY1).size());
    }

    @Test
    public void sDiffStore() {
        delAll();
        redisService.sAdd(KEY, List.of(VALUE, VALUE, VALUE1, VALUE2));
        redisService.sAdd(KEY1, List.of(VALUE1, VALUE2));
        assertEquals(1, redisService.sDiffStore(KEY, KEY1, KEY2).longValue());
    }

    @Test
    public void sInter() {
        delAll();
        redisService.sAdd(KEY, List.of(VALUE, VALUE, VALUE1, VALUE2));
        redisService.sAdd(KEY1, List.of(VALUE1, VALUE2));
        assertEquals(2, redisService.sInter(KEY, KEY1).size());
    }

    @Test
    public void sInterStore() {
        delAll();
        redisService.sAdd(KEY, List.of(VALUE, VALUE, VALUE1, VALUE2));
        redisService.sAdd(KEY1, List.of(VALUE1, VALUE2));
        assertEquals(2, redisService.sInterStore(KEY, KEY1, KEY2).intValue());
    }

    @Test
    public void sIsMember() {
        delAll();
        redisService.sAdd(KEY, VALUE);
        assertTrue(redisService.sIsMember(KEY, VALUE));
    }

    @Test
    public void sMembers() {
        delAll();
        redisService.sAdd(KEY, VALUE);
        assertEquals(1, redisService.sMembers(KEY).size());
    }

    @Test
    public void sMove() {
        delAll();
        redisService.sAdd(KEY, VALUE);
        assertTrue(redisService.sMove(KEY, KEY1, VALUE));
        assertEquals(0, redisService.sCard(KEY).intValue());
    }

    @Test
    public void sPop() {
        delAll();
        redisService.sAdd(KEY, new String[]{VALUE, VALUE, VALUE1, VALUE2});
        redisService.sPop(KEY);
        assertEquals(2, redisService.sCard(KEY).intValue());
    }

    @Test
    public void sRandMember() {
        delAll();
        List<String> os = List.of(VALUE, VALUE1, VALUE2);
        redisService.sAdd(KEY, os);
        assertTrue(os.contains(redisService.sRandMember(KEY)));
        assertTrue(os.contains(redisService.sRandMember(KEY, 2).get(1)));
    }


    @Test
    public void sRem() {
        delAll();
        List<String> os = List.of(VALUE, VALUE1, VALUE2);
        redisService.sAdd(KEY, os);
        redisService.sRem(KEY, VALUE1);
        redisService.sRem(KEY, List.of(VALUE, VALUE2));
        assertEquals(0, redisService.sCard(KEY).intValue());

    }

    @Test
    public void sUnion() {
        delAll();
        redisService.sAdd(KEY, VALUE);
        redisService.sAdd(KEY1, VALUE1);
        assertEquals(Set.of(VALUE, VALUE1), redisService.sUnion(KEY, KEY1));
    }

    @Test
    public void sUnionStore() {
        delAll();
        redisService.sAdd(KEY, VALUE);
        redisService.sAdd(KEY1, VALUE1);
        assertEquals(2, redisService.sUnionStore(KEY, KEY1, KEY2).intValue());
    }

    @Test(expected = RuntimeException.class)
    public void sScan() {
        delAll();
        redisService.sScan(KEY);
    }

    @Test
    public void zAdd() {
        delAll();
        redisService.zAdd(KEY, VALUE, 6.99D);
        redisService.zAdd(KEY, VALUE1, 5.99D);
        redisService.zAdd(KEY, VALUE2, 8.99D);
        assertEquals(3, redisService.zCard(KEY).intValue());
        redisService.zAdd(KEY, Map.of(VALUE + 3, 2.86, VALUE + 4, 8.58));
        assertEquals(5, redisService.zCard(KEY).intValue());
    }

    @Test
    public void zCard() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(3, redisService.zCard(KEY).intValue());
    }

    @Test
    public void zCount() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(2, redisService.zCount(KEY, 2.86D, 5.88D).intValue());
    }

    @Test
    public void zIncrBy() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(4.96, redisService.zIncrBy(KEY, 2.1D, VALUE).intValue(), 2.1D);
    }

    @Test
    public void zDecrBy() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(0.76, redisService.zDecrBy(KEY, 2.1D, VALUE).intValue(), 2.1D);
    }

    @Test
    public void zInterStore() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        redisService.zAdd(KEY1, Map.of(VALUE1, 8.58));
        assertEquals(1, redisService.zInterStore(KEY, KEY1, KEY2).intValue());
    }

    @Test
    public void zRange() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(Set.of(VALUE2, VALUE1), redisService.zRange(KEY, 1, 2));
    }

    @Test
    public void zRangeByScore() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(2, redisService.zRangeByScore(KEY, 5.88, 8.58).size());
    }

    @Test
    public void zRank() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(2, redisService.zRank(KEY, VALUE1).longValue());
    }

    @Test
    public void zRem() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE2, 5.88));
        assertEquals(1, redisService.zRem(KEY, VALUE1).longValue());
        assertEquals(2, redisService.zRem(KEY, List.of(VALUE, VALUE2)).longValue());
    }


    @Test
    public void zRemRangeByScore() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE + 3, 6.58, VALUE2, 5.88));
        assertEquals(3, redisService.zRemRangeByScore(KEY, 5.88, 8.88).longValue());
    }

    @Test
    public void zRemRangeByRank() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE + 3, 6.58, VALUE2, 5.88));
        redisService.zRemRangeByRank(KEY, 0, 1);
        assertEquals(2, redisService.zCard(KEY).intValue());
    }

    @Test
    public void zRevRange() {
        delAll();
        redisService.zAdd(KEY, Map.of(
                VALUE + 3, 6.58,
                VALUE1, 8.58,
                VALUE + 4, 9.86,
                VALUE2, 5.88,
                VALUE, 2.86));
        Set<String> os = redisService.zRevRange(KEY, 1, 3);
        assertEquals(Set.of(VALUE1, VALUE + 3, VALUE2), os);
    }

    @Test
    public void zRevRangeByScore() {
        delAll();
        redisService.zAdd(KEY, Map.of(
                VALUE + 3, 6.58,
                VALUE1, 8.58,
                VALUE + 4, 9.86,
                VALUE2, 5.88,
                VALUE, 2.86));
        Set<String> os = redisService.zRevRangeByScore(KEY, 8.58, 2.86);
        assertEquals(Set.of(VALUE1, VALUE + 3, VALUE2, VALUE), os);
    }

    @Test
    public void zRevRank() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE + 3, 6.58, VALUE2, 5.88));
        assertEquals(1, redisService.zRevRank(KEY, VALUE + 3).intValue());
    }

    @Test
    public void zScore() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE + 3, 6.58, VALUE2, 5.88));

        assertEquals(5.88, redisService.zScore(KEY, VALUE2), 0);
    }

    @Test
    public void zUnionStore() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE + 3, 6.58, VALUE2, 5.88));
        redisService.zAdd(KEY1, Map.of(VALUE, 2.86, VALUE1, 8.58, VALUE + 4, 6.85));
        assertEquals(5, redisService.zUnionStore(KEY, KEY1, KEY2).intValue());
    }


    @Test(expected = RuntimeException.class)
    public void zScan() {
        delAll();
        redisService.zScan(KEY);
    }

    @Test
    public void del() {
        delAll();
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE + 3, 6.58, VALUE2, 5.88));
        redisService.del(KEY);
        assertEquals(0, redisService.zCard(KEY).intValue());
        redisService.zAdd(KEY, Map.of(VALUE, 2.86, VALUE + 3, 6.58, VALUE2, 5.88));
        redisService.zAdd(KEY2, Map.of(VALUE, 2.86, VALUE + 3, 6.58, VALUE2, 5.88));
        redisService.del(List.of(KEY, KEY2));
        assertNull(redisService.get(KEY));

    }

    @Test
    public void exists() {
        delAll();
        redisService.set(KEY, VALUE);
        assertTrue(redisService.exists(KEY));
    }

    @Test
    public void expire() {
        delAll();
        redisService.set(KEY, VALUE);
        assertTrue(redisService.expire(KEY, TIMEOUT_SECONDS, TimeUnit.SECONDS));
    }


    @Test
    public void expireAt() {
        delAll();
        redisService.set(KEY, VALUE);
        assertTrue(redisService.expireAt(KEY, new Date(new Date().getTime() + 5000)));
    }

    @Test
    public void pExpire() {
        delAll();
        redisService.set(KEY, VALUE);
        assertTrue(redisService.pExpire(KEY, TIMEOUT_MILLISECONDS));
    }

    @Test
    public void pExpireAt() {
        delAll();
        redisService.set(KEY, VALUE);
        assertTrue(redisService.pExpireAt(KEY, new Date(new Date().getTime() + 5000)));
    }

    @Test
    public void persist() {
        delAll();
        redisService.set(KEY, VALUE);
        redisService.expire(KEY, TIMEOUT_SECONDS);
        assertTrue(redisService.persist(KEY));
        sleep();
        System.out.println(redisService.ttl(KEY));
    }

    @Test
    public void pTtl() {
        delAll();
        redisService.set(KEY, VALUE);
        redisService.expire(KEY, TIMEOUT_SECONDS);
        assertTrue(redisService.pTtl(KEY) > 1000);
    }

    @Test
    public void ttl() {
        delAll();
        redisService.set(KEY, VALUE);
        redisService.expire(KEY, TIMEOUT_SECONDS);
        System.out.println(redisService.ttl(KEY));
    }

    @Test
    public void randomKey() {
        delAll();
        redisService.set(KEY, VALUE);
        redisService.set(KEY1, VALUE1);
        redisService.set(KEY2, VALUE2);
        redisService.set(KEY + 3, VALUE + 3);
        assertTrue(redisService.randomKey().startsWith(KEY));
    }

    @Test
    public void rename() {
        delAll();
        redisService.set(KEY, VALUE);
        redisService.rename(KEY, KEY1);
        assertNull(redisService.get(KEY));
        assertNotNull(redisService.get(KEY1));
    }

    @Test
    public void renameNx() {
        delAll();
        redisService.set(KEY1, VALUE);
        assertTrue(redisService.renameNx(KEY1, VALUE1));
        assertFalse(redisService.renameNx(VALUE1, VALUE1));
        redisService.del(VALUE1);
    }

    @Test
    public void type() {
        delAll();
        redisService.set(KEY, VALUE);
        assertEquals(DataType.STRING, redisService.type(KEY));
    }
}