package com.wdcloud.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 赵秀非
 */
@SuppressWarnings({"JavaDoc", "SpringJavaAutowiredFieldsWarningInspection"})
@Slf4j
public class RedisService implements IRedisService {
    private RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RedisTemplate<String, Object> redisTemplate() {
        return redisTemplate;
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public String getRange(String key, int start, int end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    @Override
    public Object getSet(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    @Override
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    @Override
    public List<Object> mGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public Boolean setBit(String key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    @Override
    public void setEx(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public Boolean setNx(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    @Override
    public void setRange(String key, int offset, Object value) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    @Override
    public Long strLen(String key) {
        return redisTemplate.opsForValue().size(key);
    }

    @Override
    public void mSet(Map<String, Object> keyValues) {
        redisTemplate.opsForValue().multiSet(keyValues);
    }

    @Override
    public void mSetNx(Map<String, Object> keyValues) {
        throw new RuntimeException("未实现");
    }

    @Override
    public void pSetEx(String key, Object value, long timeout) {
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
    public void hDel(String key, Collection<String> hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public Boolean hExists(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Map<String, Object> hGetAll(String key) {
        final Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        LinkedHashMap<String, Object> reMaps = new LinkedHashMap<>(entries.values().size());
        entries.forEach((k, v) -> reMaps.put((String) k, v));
        return reMaps;
    }

    @Override
    public void hIncr(String key, String hashKey) {
        hIncrBy(key, hashKey, 1);
    }

    @Override
    public void hIncrBy(String key, String hashKey, long delta) {
        redisTemplate.opsForHash().increment(key, hashKey, Math.abs(delta));
    }

    @Override
    public void hIncrByFloat(String key, String hashKey, double delta) {
        redisTemplate.opsForHash().increment(key, hashKey, Math.abs(delta));
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
    public List<Object> hMget(String key, Collection<String> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, Collections.singleton(hashKeys));
    }

    @Override
    public void hMset(String key, Map<String, Object> hashKeyValues) {
        redisTemplate.opsForHash().putAll(key, hashKeyValues);
    }

    @Override
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void hSetNx(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    @Override
    public List<Object> hVals(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    @Override
    public void hScan(String key) {
        throw new RuntimeException("未实现");
    }

    @Override
    public Object bLpop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    @Override
    public Object rLpop(String key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    @Override
    public Object bRpopLpush(String srcKey, String targetKey, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(srcKey, targetKey, timeout, unit);
    }

    @Override
    public Long lInsert(String key, Object pivot, Object value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    @Override
    public Long lLen(String key) {
        return redisTemplate.opsForList().size(key);
    }


    @Override
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    @Override
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public Long lPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public Long lPushX(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    @Override
    public List<Object> lRange(String key, long start, long stop) {
        return redisTemplate.opsForList().range(key, start, stop);
    }

    @Override
    public Long lRem(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    @Override
    public void lSet(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    @Override
    public void lTrim(String key, long start, long stop) {
        redisTemplate.opsForList().trim(key, start, stop);
    }

    @Override
    public Object rPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public Object rPopLpush(String srcKey, String targetKey) {

        return redisTemplate.opsForList().rightPopAndLeftPush(srcKey, targetKey);
    }

    @Override
    public Long rPush(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    @Override
    public Long rPushX(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    @Override
    public Long sAdd(String key, Object member) {
        return redisTemplate.opsForSet().add(key, member);
    }

    @Override
    public Long sAdd(String key, Collection<Object> members) {
        return redisTemplate.opsForSet().add(key, members);
    }

    @Override
    public Long sCard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Set<Object> sDiff(String key1, String key2) {
        return redisTemplate.opsForSet().difference(key1, key2);
    }

    @Override
    public Long sDiffStore(String key1, String key2, String targetKey) {
        return redisTemplate.opsForSet().differenceAndStore(key1, key2, targetKey);
    }

    @Override
    public Set<Object> sInter(String key1, String key2) {
        return redisTemplate.opsForSet().intersect(key1, key2);
    }

    @Override
    public Long sInterStore(String key1, String key2, String targetKey) {
        return redisTemplate.opsForSet().intersectAndStore(key1, key2, targetKey);
    }

    @Override
    public Boolean sIsMember(String key, Object member) {
        return redisTemplate.opsForSet().isMember(key, member);
    }

    @Override
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Boolean sMove(String srcKey, String targetKey, Object member) {
        return redisTemplate.opsForSet().move(srcKey, member, targetKey);
    }

    @Override
    public Object sPop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    @Override
    public Object sRandMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public List<Object> sRandMember(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    @Override
    public Long sRem(String key, Object member) {
        return redisTemplate.opsForSet().remove(key, member);
    }

    @Override
    public Long sRem(String key, Collection<Object> members) {
        return redisTemplate.opsForSet().remove(key, members);
    }

    @Override
    public Set<Object> sUnion(String key1, String key2) {
        return redisTemplate.opsForSet().union(key1, key2);
    }

    @Override
    public Set<Object> sUnion(String key1, Collection<String> keys) {
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
    public Boolean zAdd(String key, Object member, double score) {
        return redisTemplate.opsForZSet().add(key, member, score);
    }

    @Override
    public Long zAdd(String key, Map<Object, Double> members) {
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
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
    public Double zIncrBy(String key, double delta, Object member) {
        return redisTemplate.opsForZSet().incrementScore(key, member, Math.abs(delta));
    }

    @Override
    public Double zDecrBy(String key, double delta, Object member) {
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
    public Long zLexCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @Override
    public Set<Object> zRange(String key, long start, long stop) {
        return redisTemplate.opsForZSet().range(key, start, stop);
    }

    @Override
    public Set<Object> zRangeByLex(String key, double min, double max) {
        throw new RuntimeException("未实现");
    }

    @Override
    public Set<Object> zRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public Long zRank(String key, Object member) {
        return redisTemplate.opsForZSet().rank(key, member);
    }

    @Override
    public Long zRem(String key, Object member) {
        return redisTemplate.opsForZSet().remove(key, List.of(member));
    }

    @Override
    public Long zRem(String key, Collection<Object> members) {
        return redisTemplate.opsForZSet().remove(key, members);
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
    public Set<Object> zRevRange(String key, long start, long stop) {
        return redisTemplate.opsForZSet().reverseRange(key, start, stop);
    }

    @Override
    public Set<Object> zRevRangeByScore(String key, double start, double stop) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, start, stop);
    }

    @Override
    public Long zRevRank(String key, Object member) {
        return redisTemplate.opsForZSet().reverseRank(key, member);
    }

    @Override
    public Double zScore(String key, Object member) {
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

