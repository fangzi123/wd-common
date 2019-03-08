package com.wdcloud.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 赵秀非
 */
@SuppressWarnings({"JavaDoc", "SpringJavaAutowiredFieldsWarningInspection"})
@Slf4j
public class RedisService implements IRedisService {
    private RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisTemplate<String, String> redisTemplate() {
        return redisTemplate;
    }

    @Override
    public synchronized boolean lock(String lockKey, int seconds) {
        if (StringUtils.isEmpty(lockKey)) {
            log.error("lock parameter error. lockKey={}, seconds={}", lockKey, seconds);
            return false;
        }
        long lockExpireTime = System.currentTimeMillis() + seconds * 1000 + 1;//锁超时时间
        if (setNx(lockKey, lockExpireTime + "")) { // 获取到锁
            //成功获取到锁, 设置相关标识
            expire(lockKey, seconds);
            RedisLockThread.setThreadMap(Thread.currentThread().getId(), Thread.currentThread());
            log.info("lock success. lockKey={}, seconds={}", lockKey, seconds);
            return true;
        }
        log.info("lock error. lockKey={}, seconds={}", lockKey, seconds);
        return false;
    }


    @Override
    public synchronized void unLock(String lockKey) {
        if (StringUtils.isEmpty(lockKey)) {
            log.error("unLock parameter error. lockKey={}", lockKey);
            return;
        }
        // 避免删除非自己获取得到的锁
        Thread thread = RedisLockThread.getThread(Thread.currentThread().getId());
        if (thread != null) {
            del(lockKey);
            //删除MAP中的线程记录
            RedisLockThread.removeThread(Thread.currentThread().getId());
            log.info("unLock success. lockKey={}", lockKey);
        }
    }

    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    @Override
    public String getRange(String key, int start, int end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    @Override
    public String getSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }


    @Override
    public List<String> mGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void setEx(String key, String value, long timeout) {
        setEx(key, value, timeout, TimeUnit.SECONDS);
    }

    @Override
    public void setEx(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public Boolean setNx(String key, String value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public void setRange(String key, int offset, String value) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    @Override
    public Long strLen(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    @Override
    public void mSet(Map<String, String> keyValues) {
        redisTemplate.opsForValue().multiSet(keyValues);
    }

    @Override
    public Boolean mSetNx(Map<String, String> keyValues) {
        return redisTemplate.opsForValue().multiSetIfAbsent(keyValues);
    }

    @Override
    public void pSetEx(String key, String value, long timeout) {
        setEx(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Long incr(String key) {
        return incrBy(key, 1);
    }

    @Override
    public Long incrBy(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, Math.abs(delta));
    }

    @Override
    public Double incrByFloat(String key, double delta) {
        return redisTemplate.opsForValue().increment(key, Math.abs(delta));
    }

    @Override
    public Long decr(String key) {
        return decrBy(key, -1);
    }

    @Override
    public Long decrBy(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta > 0 ? -delta : delta);
    }

    @Override
    public Double decrByFloat(String key, double delta) {
        return redisTemplate.opsForValue().increment(key, delta > 0 ? -delta : delta);
    }

    @Override
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    @Override
    public void hDel(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public void hDel(String key, Collection<String> hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public Boolean hExists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public String hGet(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        final Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        LinkedHashMap<String, String> reMaps = new LinkedHashMap<>(entries.values().size());
        entries.forEach((k, v) -> reMaps.put((String) k, (String) v));
        return reMaps;
    }

    @Override
    public Long hIncr(String key, String hashKey) {
        return hIncrBy(key, hashKey, 1);
    }

    @Override
    public Long hIncrBy(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, Math.abs(delta));
    }

    @Override
    public Double hIncrByFloat(String key, String hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, Math.abs(delta));
    }

    @Override
    public Long hDecr(String key, String hashKey) {
        return hDecrBy(key, hashKey, -1);
    }

    @Override
    public Long hDecrBy(String key, String hashKey, long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta > 0 ? -delta : delta);
    }

    @Override
    public Double hDecrByFloat(String key, String hashKey, double delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta > 0 ? -delta : delta);
    }

    @Override
    public Set<String> hKeys(String key) {
        final Set<Object> keys = redisTemplate.opsForHash().keys(key);
        final LinkedHashSet<String> reKeys = new LinkedHashSet<>();
        keys.forEach(o -> reKeys.add((String) o));
        return reKeys;
    }

    @Override
    public Long hLen(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public List<String> hMget(String key, Collection<String> hashKeys) {
        List<Object> objects = redisTemplate.opsForHash().multiGet(key, new HashSet<>(hashKeys));
        List<String> values = new ArrayList<>(objects.size());
        objects.forEach(o -> values.add((String) o));
        return values;
    }

    @Override
    public void hMset(String key, Map<String, String> hashKeyValues) {
        redisTemplate.opsForHash().putAll(key, hashKeyValues);
    }

    @Override
    public void hSet(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void hSetNx(String key, String hashKey, String value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    @Override
    public List<String> hVals(String key) {
        List<Object> objects = redisTemplate.opsForHash().values(key);
        List<String> values = new ArrayList<>(objects.size());
        objects.forEach(o -> values.add((String) o));
        return values;
    }

    @Override
    public void hScan(String key) {
        throw new RuntimeException("未实现");
    }

    @Override
    public String bLpop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    @Override
    public String rLpop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    @Override
    public String bRpopLpush(String srcKey, String targetKey, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(srcKey, targetKey, timeout, unit);
    }

    @Override
    public Long lInsert(String key, String pivot, String value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    @Override
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }


    @Override
    public String lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public String lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public Long lPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public Long lPushX(String key, String value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    @Override
    public List<String> lRange(String key, long start, long stop) {
        return redisTemplate.opsForList().range(key, start, stop);
    }

    @Override
    public Long lRem(String key, long count, String value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public void lSet(String key, long index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void lTrim(String key, long start, long stop) {
        redisTemplate.opsForList().trim(key, start, stop);
    }

    @Override
    public String rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public String rPopLpush(String srcKey, String targetKey) {

        return redisTemplate.opsForList().rightPopAndLeftPush(srcKey, targetKey);
    }

    @Override
    public Long rPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Long rPush(String key, Collection<String> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long rPushX(String key, String value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    @Override
    public Long sAdd(String key, String member) {
        return redisTemplate.opsForSet().add(key, member);
    }

    @Override
    public Long sAdd(String key, Collection<String> members) {
        String[] ms = members.toArray(new String[0]);
        return sAdd(key, ms);
    }

    @Override
    public Long sAdd(String key, String[] members) {
        return redisTemplate.opsForSet().add(key, members);
    }

    @Override
    public Long sCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Set<String> sDiff(String key1, String key2) {
        return redisTemplate.opsForSet().difference(key1, key2);
    }

    @Override
    public Long sDiffStore(String key1, String key2, String targetKey) {
        return redisTemplate.opsForSet().differenceAndStore(key1, key2, targetKey);
    }

    @Override
    public Set<String> sInter(String key1, String key2) {
        return redisTemplate.opsForSet().intersect(key1, key2);
    }

    @Override
    public Long sInterStore(String key1, String key2, String targetKey) {
        return redisTemplate.opsForSet().intersectAndStore(key1, key2, targetKey);
    }

    @Override
    public Boolean sIsMember(String key, String member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    @Override
    public Set<String> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Boolean sMove(String srcKey, String targetKey, String member) {
        return redisTemplate.opsForSet().move(srcKey, member, targetKey);
    }

    @Override
    public String sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    @Override
    public String sRandMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public List<String> sRandMember(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    @Override
    public Long sRem(String key, String member) {
        return redisTemplate.opsForSet().remove(key, member);
    }

    @Override
    public Long sRem(String key, String[] members) {
        return redisTemplate.opsForSet().remove(key, (Object[]) members);
    }

    @Override
    public Long sRem(String key, Collection<String> members) {
        String[] ms = members.toArray(new String[0]);
        return sRem(key, ms);
    }

    @Override
    public Set<String> sUnion(String key1, String key2) {
        return redisTemplate.opsForSet().union(key1, key2);
    }

    @Override
    public Set<String> sUnion(String key1, Collection<String> keys) {
        return redisTemplate.opsForSet().union(key1, keys);
    }

    @Override
    public Long sUnionStore(String key1, String keys2, String targetKey) {
        return redisTemplate.opsForSet().unionAndStore(key1, keys2, targetKey);
    }

    @Override
    public Long sUnionStore(String key1, Collection<String> key2, String targetKey) {
        return redisTemplate.opsForSet().unionAndStore(key1, key2, targetKey);

    }

    @Override
    public void sScan(String key) {
        throw new RuntimeException("未实现");
    }

    @Override
    public Boolean zAdd(String key, String member, double score) {
        return redisTemplate.opsForZSet().add(key, member, score);
    }

    @Override
    public Long zAdd(String key, Map<String, Double> members) {
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        members.forEach((k, v) -> tuples.add(new DefaultTypedTuple<>(k, v)));
        return redisTemplate.opsForZSet().add(key, tuples);
    }

    @Override
    public Long zCard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Long zCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @Override
    public Double zIncrBy(String key, double delta, String member) {
        return redisTemplate.opsForZSet().incrementScore(key, member, Math.abs(delta));
    }

    @Override
    public Double zDecrBy(String key, double delta, String member) {
        return redisTemplate.opsForZSet().incrementScore(key, member, delta > 0 ? -delta : delta);

    }

    @Override
    public Long zInterStore(String key1, String key2, String targetKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key1, key2, targetKey);
    }

    @Override
    public Long zInterStore(String key1, Collection<String> key2, String targetKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key1, key2, targetKey);
    }

    @Override
    public Set<String> zRange(String key, long start, long stop) {
        return redisTemplate.opsForZSet().range(key, start, stop);
    }

    @Override
    public Set<String> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public Long zRank(String key, String member) {
        return redisTemplate.opsForZSet().rank(key, member);
    }

    @Override
    public Long zRem(String key, String member) {
        return redisTemplate.opsForZSet().remove(key, member);
    }

    @Override
    public Long zRem(String key, Collection<String> members) {
        return redisTemplate.opsForZSet().remove(key, members.toArray(new Object[0]));
    }

    @Override
    public Long zRemRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @Override
    public Long zRemRangeByRank(String key, long start, long stop) {
        return redisTemplate.opsForZSet().removeRange(key, start, stop);
    }

    @Override
    public Set<String> zRevRange(String key, long start, long stop) {
        return redisTemplate.opsForZSet().reverseRange(key, start, stop);
    }

    @Override
    public Set<String> zRevRangeByScore(String key, double max, double min) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    @Override
    public Long zRevRank(String key, String member) {
        return redisTemplate.opsForZSet().reverseRank(key, member);
    }

    @Override
    public Double zScore(String key, String member) {
        return redisTemplate.opsForZSet().score(key, member);
    }

    @Override
    public Long zUnionStore(String key1, String key2, String targetKey) {
        return redisTemplate.opsForZSet().unionAndStore(key1, key2, targetKey);
    }

    @Override
    public Long zUnionStore(String key1, Collection<String> key2, String targetKey) {
        return redisTemplate.opsForZSet().unionAndStore(key1, key2, targetKey);
    }

    @Override
    public void zScan(String key) {
        throw new RuntimeException("未实现");
    }

    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Long del(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    @Override
    public Boolean pExpire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean pExpireAt(String key, Date date) {
        return expireAt(key, date);
    }

    @Override
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    @Override
    public Long pTtl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
    }

    @Override
    public Long ttl(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    @Override
    public void rename(String key, String newKey) {
        redisTemplate.rename(key, newKey);
    }

    @Override
    public Boolean renameNx(String key, String newKey) {
        return redisTemplate.renameIfAbsent(key, newKey);
    }

    @Override
    public DataType type(String key) {
        return redisTemplate.type(key);
    }
}

