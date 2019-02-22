package com.wdcloud.redis;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 赵秀非
 */
@SuppressWarnings({"JavaDoc", "SpringJavaAutowiredFieldsWarningInspection"})
public interface IRedisService {
    RedisTemplate<String, String> redisTemplate();

    /**
     * 获取阻塞锁
     *
     * @param lockKey key
     * @param seconds 有效时间（秒） 当传入0时，无失效时间
     * @return boolean
     */
    boolean lock(String lockKey, int seconds);

    /**
     * 释放锁
     *
     * @param lockKey key
     */
    void unLock(String lockKey);

    /**
     * SET key value
     * 设置指定 key 的值
     *
     * @param key
     * @param value
     * @return
     */
    void set(String key, String value);

    /**
     * GET key
     * 获取指定 key 的值。
     */
    String get(String key);

    /**
     * GETRANGE key start end
     * 返回 key 中字符串值的子字符
     */
    String getRange(String key, int start, int end);

    /**
     * GETSET key value
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     */
    String getSet(String key, String value);


    /**
     * MGET key1 [key2..]
     * 获取所有(一个或多个)给定 key 的值。
     */
    List<String> mGet(Collection<String> keys);

    /**
     * SETEX key seconds value
     * 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。
     */
    void setEx(String key, String value, long timeout);

    void setEx(String key, String value, long timeout, TimeUnit unit);

    /**
     * SETNX key value
     * 只有在 key 不存在时设置 key 的值。
     */
    Boolean setNx(String key, String value);

    /**
     * SETRANGE key offset value
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。
     */
    void setRange(String key, int offset, String value);

    /**
     * STRLEN key
     * 返回 key 所储存的字符串值的长度。
     */
    Long strLen(String key);

    /**
     * MSET key value [key value ...]
     * 同时设置一个或多个 key-value 对。
     */
    void mSet(Map<String, String> keyValues);

    /**
     * MSETNX key value [key value ...]
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
     */
    Boolean mSetNx(Map<String, String> keyValues);

    /**
     * PSETEX key milliseconds value
     * 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。
     */
    void pSetEx(String key, String value, long timeout);

    /**
     * INCR key
     * 将 key 中储存的数字值增一。
     */
    Long incr(String key);

    Long incrBy(String key, long delta);

    Double incrByFloat(String key, double delta);

    /**
     * DECR key
     * 将 key 中储存的数字值减一。
     */
    Long decr(String key);

    Long decrBy(String key, long delta);

    Double decrByFloat(String key, double delta);

    /**
     * APPEND key value
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。
     */
    Integer append(String key, String value);

    /**
     * HDEL key field1 [field2]
     * 删除一个或多个哈希表字段
     */
    void hDel(String key, Collection<String> hashKeys);

    void hDel(String key, String hashKey);

    /**
     * HEXISTS key field
     * 查看哈希表 key 中，指定的字段是否存在。
     */
    Boolean hExists(String key, String hashKey);

    /**
     * HGET key field
     * 获取存储在哈希表中指定字段的值。
     */
    String hGet(String key, String hashKey);

    /**
     * HGETALL key
     * 获取在哈希表中指定 key 的所有字段和值
     */
    Map<String, String> hGetAll(String key);

    /**
     * HINCRBY key field increment
     * 为哈希表 key 中的指定字段的整数值加上增量 increment 。
     */
    Long hIncr(String key, String hashKey);

    Long hIncrBy(String key, String hashKey, long delta);

    Double hIncrByFloat(String key, String hashKey, double delta);

    Long hDecr(String key, String hashKey);

    Long hDecrBy(String key, String hashKey, long delta);

    Double hDecrByFloat(String key, String hashKey, double delta);

    /**
     * HKEYS key
     * 获取所有哈希表中的字段
     */
    Set<String> hKeys(String key);

    /**
     * HLEN key
     * 获取哈希表中字段的数量
     */
    Long hLen(String key);

    /**
     * HMGET key field1 [field2]
     * 获取所有给定字段的值
     */
    List<String> hMget(String key, Collection<String> hashKeys);

    /**
     * HMSET key field1 value1 [field2 value2 ]
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     */
    void hMset(String key, Map<String, String> hashKeyValues);

    /**
     * HSET key field value
     * 将哈希表 key 中的字段 field 的值设为 value 。
     */
    void hSet(String key, String hashKey, String value);

    /**
     * HSETNX key field value
     * 只有在字段 field 不存在时，设置哈希表字段的值。
     */
    void hSetNx(String key, String hashKey, String value);

    /**
     * HVALS key
     * 获取哈希表中所有值
     */
    List<String> hVals(String key);

    /**
     * HSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代哈希表中的键值对。
     */
    void hScan(String key);

    /**
     * BLPOP key1 [key2 ] timeout
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    String bLpop(String key, long timeout, TimeUnit unit);

    /**
     * BRPOP key1 [key2 ] timeout
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    String rLpop(String key, long timeout, TimeUnit unit);

    /**
     * BRPOPLPUSH source destination timeout
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    String bRpopLpush(String srcKey, String targetKey, long timeout, TimeUnit unit);

    /**
     * LINDEX key index
     * 通过索引获取列表中的元素
     */
    String lIndex(String key, long index);

    /**
     * LINSERT key BEFORE|AFTER pivot value
     * 在列表的元素前或者后插入元素
     */
    Long lInsert(String key, String pivot, String value);

    /**
     * LLEN key
     * 获取列表长度
     */
    Long lLen(String key);

    /**
     * LPOP key
     * 移出并获取列表的第一个元素
     */
    String lPop(String key);

    /**
     * LPUSH key value1 [value2]
     * 将一个或多个值插入到列表头部
     */
    Long lPush(String key, String value);

    /**
     * LPUSHX key value
     * 将一个值插入到已存在的列表头部
     */
    Long lPushX(String key, String value);

    /**
     * LRANGE key start stop
     * 获取列表指定范围内的元素
     */
    List<String> lRange(String key, long start, long stop);

    /**
     * LREM key count value
     * 移除列表元素
     */
    Long lRem(String key, long count, String value);

    /**
     * LSET key index value
     * 通过索引设置列表元素的值
     */
    void lSet(String key, long index, String value);

    /**
     * LTRIM key start stop
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     */
    void lTrim(String key, long start, long stop);

    /**
     * RPOP key
     * 移除列表的最后一个元素，返回值为移除的元素。
     */
    String rPop(String key);

    /**
     * RPOPLPUSH source destination
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     */
    String rPopLpush(String srcKey, String targetKey);

    /**
     * RPUSH key value1 [value2]
     * 在列表尾部添加一个或多个值
     */
    Long rPush(String key, Collection<String> values);

    Long rPush(String key, String value);

    /**
     * RPUSHX key value
     * 为已存在的列表尾部添加值
     */
    Long rPushX(String key, String value);

    /**
     * SADD key member1 [member2]
     * 向集合添加一个或多个成员
     */
    Long sAdd(String key, String member);

    Long sAdd(String key, String[] members);

    Long sAdd(String key, Collection<String> members);

    /**
     * SCARD key
     * 获取集合的成员数
     */
    Long sCard(String key);

    /**
     * SDIFF key1 [key2]null
     * 返回给定所有集合的差集
     */
    Set<String> sDiff(String key1, String key2);

    /**
     * SDIFFSTORE destination key1 [key2]
     * 返回给定所有集合的差集并存储在 destination 中
     */
    Long sDiffStore(String key1, String key2, String targetKey);

    /**
     * SINTER key1 [key2]
     * 返回给定所有集合的交集
     */
    Set<String> sInter(String key1, String key2);

    /**
     * SINTERSTORE destination key1 [key2]
     * 返回给定所有集合的交集并存储在 destination 中
     */
    Long sInterStore(String key1, String key2, String targetKey);

    /**
     * SISMEMBER key member
     * 判断 member 元素是否是集合 key 的成员
     */
    Boolean sIsMember(String key, String member);

    /**
     * SMEMBERS key
     * 返回集合中的所有成员
     */
    Set<String> sMembers(String key);

    /**
     * SMOVE source destination member
     * 将 member 元素从 source 集合移动到 destination 集合
     */
    Boolean sMove(String srcKey, String targetKey, String member);

    /**
     * SPOP key
     * 移除并返回集合中的一个随机元素
     */
    String sPop(String key);


    String sRandMember(String key);

    /**
     * SRANDMEMBER key [count]
     * 返回集合中一个或多个随机数
     */
    List<String> sRandMember(String key, long count);


    Long sRem(String key, String member);

    /**
     * SREM key member1 [member2]
     * 移除集合中一个或多个成员
     */
    Long sRem(String key, String[] members);

    Long sRem(String key, Collection<String> members);

    /**
     * SUNION key1 [key2]
     * 返回所有给定集合的并集
     */
    Set<String> sUnion(String key1, String key2);

    /**
     * SUNION key1 [key2]
     * 返回所有给定集合的并集
     */
    Set<String> sUnion(String key1, Collection<String> keys);

    /**
     * SUNIONSTORE destination key1 [key2]
     * 所有给定集合的并集存储在 destination 集合中
     */
    Long sUnionStore(String key1, String key2, String targetKey);

    Long sUnionStore(String key1, Collection<String> key2, String targetKey);

    /**
     * SSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代集合中的元素
     */
    void sScan(String key);

    /**
     * ZADD key score1 member1 [score2 member2]
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     */
    Boolean zAdd(String key, String member, double score);

    Long zAdd(String key, Map<String, Double> members);

    /**
     * ZCARD key
     * 获取有序集合的成员数
     */
    Long zCard(String key);

    /**
     * ZCOUNT key min max
     * 计算在有序集合中指定区间分数的成员数
     */
    Long zCount(String key, double min, double max);

    /**
     * ZINCRBY key increment member
     * 有序集合中对指定成员的分数加上增量 increment
     */
    Double zIncrBy(String key, double delta, String member);

    Double zDecrBy(String key, double delta, String member);

    /**
     * ZINTERSTORE destination numkeys key [key ...]
     * 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
     */
    Long zInterStore(String key1, String key2, String targetKey);

    /**
     * ZINTERSTORE destination numkeys key [key ...]
     * 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
     */
    Long zInterStore(String key1, Collection<String> key2, String targetKey);


    /**
     * ZRANGE key start stop [WITHSCORES]
     * 通过索引区间返回有序集合成指定区间内的成员,成员的位置按 score 值递增(从小到大)来排序
     */
    Set<String> zRange(String key, long start, long stop);


    /**
     * ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     */
    Set<String> zRangeByScore(String key, double min, double max);

    /**
     * ZRANK key member
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     */
    Long zRank(String key, String member);

    /**
     * ZREM key member [member ...]
     * 移除有序集合中的一个或多个成员
     */
    Long zRem(String key, String member);

    Long zRem(String key, Collection<String> members);

    /**
     * ZREMRANGEBYSCORE key min max
     * 移除有序集合中给定的分数区间的所有成员
     */
    Long zRemRangeByScore(String key, double min, double max);

    /**
     * ZREMRANGEBYRANK key start stop
     * 移除有序集合中给定的排名区间的所有成员
     */
    Long zRemRangeByRank(String key, long start, long stop);


    /**
     * ZREVRANGE key start stop [WITHSCORES]
     * 返回有序集中指定区间内的成员，通过索引，分数从高到底
     */
    Set<String> zRevRange(String key, long start, long stop);

    /**
     * ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     */
    Set<String> zRevRangeByScore(String key, double max, double min);

    /**
     * ZREVRANK key member
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递减(从大到小)排序。
     * 排名以 0 为底，也就是说， score 值最大的成员排名为 0
     */
    Long zRevRank(String key, String member);

    /**
     * ZSCORE key member
     * 返回有序集中，成员的分数值
     */
    Double zScore(String key, String member);

    /**
     * ZUNIONSTORE destination numkeys key [key ...]
     * 计算给定的一个或多个有序集的并集，并存储在新的 key 中
     */
    Long zUnionStore(String key1, String key2, String targetKey);

    Long zUnionStore(String key1, Collection<String> key2, String targetKey);

    /**
     * ZSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代有序集合中的元素（包括元素成员和元素分值）
     */
    void zScan(String key);

    /**
     * DEL key
     * 该命令用于在 key 存在时删除 key。
     */
    Boolean del(String key);

    Long del(Collection<String> key);


    /**
     * EXISTS key
     * 检查给定 key 是否存在。
     */
    Boolean exists(String key);

    /**
     * EXPIRE key seconds
     * 为给定 key 设置过期时间，以秒计。
     */
    Boolean expire(String key, long timeout);

    Boolean expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * EXPIREAT key timestamp
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
     */
    Boolean expireAt(String key, Date date);

    /**
     * PEXPIRE key milliseconds
     * 设置 key 的过期时间以毫秒计。
     */
    Boolean pExpire(String key, long timeout);

    /**
     * PEXPIREAT key milliseconds-timestamp
     * 设置 key 过期时间的时间戳(unix timestamp) 以毫秒计
     */
    Boolean pExpireAt(String key, Date date);


    /**
     * PERSIST key
     * 移除 key 的过期时间，key 将持久保持。
     */
    Boolean persist(String key);

    /**
     * PTTL key
     * 以毫秒为单位返回 key 的剩余的过期时间。
     */
    Long pTtl(String key);

    /**
     * TTL key
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     */
    Long ttl(String key);

    /**
     * RANDOMKEY
     * 从当前数据库中随机返回一个 key 。
     */
    String randomKey();

    /**
     * RENAME key newkey
     * 修改 key 的名称
     */

    void rename(String key, String newKey);

    /**
     * RENAMENX key newkey
     * 仅当 newkey 不存在时，将 key 改名为 newkey 。
     */

    Boolean renameNx(String key, String newKey);

    /**
     * TYPE key
     * 返回 key 所储存的值的类型。
     */
    DataType type(String key);


}
