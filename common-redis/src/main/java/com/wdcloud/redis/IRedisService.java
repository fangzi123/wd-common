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
    RedisTemplate<String, Object> redisTemplate();

    /**
     * 1 SET key value
     * 设置指定 key 的值
     *
     * @param key
     * @param value
     * @return
     */
    void set(String key, Object value);

    /**
     * 2	GET key
     * 获取指定 key 的值。
     */
    Object get(String key);

    /**
     * 3	GETRANGE key start end
     * 返回 key 中字符串值的子字符
     */
    String getRange(String key, int start, int end);

    /**
     * 4	GETSET key value
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     */
    Object getSet(String key, String value);

    /**
     * 5	GETBIT key offset
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
     */
    Boolean getBit(String key, long offset);

    /**
     * 6	MGET key1 [key2..]
     * 获取所有(一个或多个)给定 key 的值。
     */
    List<Object> mGet(Collection<String> keys);

    /**
     * 7	SETBIT key offset value
     * 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
     */
    Boolean setBit(String key, long offset, boolean value);

    /**
     * 8	SETEX key seconds value
     * 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)。
     */
    void setEx(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 9	SETNX key value
     * 只有在 key 不存在时设置 key 的值。
     */
    Boolean setNx(String key, Object value);

    /**
     * 10	SETRANGE key offset value
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。
     */
    void setRange(String key, int offset, Object value);

    /**
     * 11	STRLEN key
     * 返回 key 所储存的字符串值的长度。
     */
    Long strLen(String key);

    /**
     * 12	MSET key value [key value ...]
     * 同时设置一个或多个 key-value 对。
     */
    void mSet(Map<String, Object> keyValues);

    /**
     * 13	MSETNX key value [key value ...]
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
     */
    void mSetNx(Map<String, Object> keyValues);

    /**
     * 14	PSETEX key milliseconds value
     * 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。
     */
    void pSetEx(String key, Object value, long timeout);

    /**
     * 15	INCR key
     * 将 key 中储存的数字值增一。
     */
    Long incr(String key);

    Long incrBy(String key, long delta);

    Double incrByFloat(String key, double delta);

    /**
     * 18	DECR key
     * 将 key 中储存的数字值减一。
     */
    Long decr(String key);

    Long decrBy(String key, long delta);

    Double decrByFloat(String key, double delta);

    /**
     * 20	APPEND key value
     * 如果 key 已经存在并且是一个字符串， APPEND 命令将指定的 value 追加到该 key 原来值（value）的末尾。
     */
    Integer append(String key, String value);

    /**
     * 1	HDEL key field1 [field2]
     * 删除一个或多个哈希表字段
     */
    void hDel(String key, Collection<String> hashKeys);

    /**
     * 2	HEXISTS key field
     * 查看哈希表 key 中，指定的字段是否存在。
     */
    Boolean hExists(String key, String hashKey);

    /**
     * 3	HGET key field
     * 获取存储在哈希表中指定字段的值。
     */
    Object hGet(String key, String hashKey);

    /**
     * 4	HGETALL key
     * 获取在哈希表中指定 key 的所有字段和值
     */
    Map<String, Object> hGetAll(String key);

    /**
     * 5	HINCRBY key field increment
     * 为哈希表 key 中的指定字段的整数值加上增量 increment 。
     */
    void hIncr(String key, String hashKey);

    void hIncrBy(String key, String hashKey, long delta);

    void hIncrByFloat(String key, String hashKey, double delta);

    Long hDecr(String key, String hashKey);

    Long hDecrBy(String key, String hashKey, long delta);

    Double hDecrByFloat(String key, String hashKey, double delta);

    /**
     * 7	HKEYS key
     * 获取所有哈希表中的字段
     */
    Set<String> hKeys(String key);

    /**
     * 8	HLEN key
     * 获取哈希表中字段的数量
     */
    Long hLen(String key);

    /**
     * 9	HMGET key field1 [field2]
     * 获取所有给定字段的值
     */
    List<Object> hMget(String key, Collection<String> hashKeys);

    /**
     * 10	HMSET key field1 value1 [field2 value2 ]
     * 同时将多个 field-value (域-值)对设置到哈希表 key 中。
     */
    void hMset(String key, Map<String, Object> hashKeyValues);

    /**
     * 11	HSET key field value
     * 将哈希表 key 中的字段 field 的值设为 value 。
     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 12	HSETNX key field value
     * 只有在字段 field 不存在时，设置哈希表字段的值。
     */
    void hSetNx(String key, String hashKey, Object value);

    /**
     * 13	HVALS key
     * 获取哈希表中所有值
     */
    List<Object> hVals(String key);

    /**
     * 14	HSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代哈希表中的键值对。
     */
    void hScan(String key);

    /**
     * 1	BLPOP key1 [key2 ] timeout
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    Object bLpop(String key, long timeout, TimeUnit unit);

    /**
     * 2	BRPOP key1 [key2 ] timeout
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    Object rLpop(String key, long timeout, TimeUnit unit);

    /**
     * 3	BRPOPLPUSH source destination timeout
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     */
    Object bRpopLpush(String srcKey, String targetKey, long timeout, TimeUnit unit);

    /**
     * 4	LINDEX key index
     * 通过索引获取列表中的元素
     */
    Object lIndex(String key, long index);

    /**
     * 5	LINSERT key BEFORE|AFTER pivot value
     * 在列表的元素前或者后插入元素
     */
    Long lInsert(String key, Object pivot, Object value);

    /**
     * 6	LLEN key
     * 获取列表长度
     */
    Long lLen(String key);

    /**
     * 7	LPOP key
     * 移出并获取列表的第一个元素
     */
    Object lPop(String keys);

    /**
     * 8	LPUSH key value1 [value2]
     * 将一个或多个值插入到列表头部
     */
    Long lPush(String key, Object value);

    /**
     * 9	LPUSHX key value
     * 将一个值插入到已存在的列表头部
     */
    Long lPushX(String key, Object value);

    /**
     * 10	LRANGE key start stop
     * 获取列表指定范围内的元素
     */
    List<Object> lRange(String key, long start, long stop);

    /**
     * 11	LREM key count value
     * 移除列表元素
     */
    Long lRem(String key, long count, Object value);

    /**
     * 12	LSET key index value
     * 通过索引设置列表元素的值
     */
    void lSet(String key, long index, Object value);

    /**
     * 13	LTRIM key start stop
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     */
    void lTrim(String key, long start, long stop);

    /**
     * 14	RPOP key
     * 移除列表的最后一个元素，返回值为移除的元素。
     */
    Object rPop(String key);

    /**
     * 15	RPOPLPUSH source destination
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     */
    Object rPopLpush(String srcKey, String targetKey);

    /**
     * 16	RPUSH key value1 [value2]
     * 在列表中添加一个或多个值
     */
    Long rPush(String key, Collection<Object> values);

    /**
     * 17	RPUSHX key value
     * 为已存在的列表添加值
     */
    Long rPushX(String key, Object value);

    /**
     * 1	SADD key member1 [member2]
     * 向集合添加一个或多个成员
     */
    Long sAdd(String key, Object member);

    Long sAdd(String key, Collection<Object> members);

    /**
     * 2	SCARD key
     * 获取集合的成员数
     */
    Long sCard(String key);

    /**
     * 3	SDIFF key1 [key2]null
     * 返回给定所有集合的差集
     */
    Set<Object> sDiff(String key1, String key2);

    /**
     * 4	SDIFFSTORE destination key1 [key2]
     * 返回给定所有集合的差集并存储在 destination 中
     */
    Long sDiffStore(String key1, String key2, String targetKey);

    /**
     * 5	SINTER key1 [key2]
     * 返回给定所有集合的交集
     */
    Set<Object> sInter(String key1, String key2);

    /**
     * 6	SINTERSTORE destination key1 [key2]
     * 返回给定所有集合的交集并存储在 destination 中
     */
    Long sInterStore(String key1, String key2, String targetKey);

    /**
     * 7	SISMEMBER key member
     * 判断 member 元素是否是集合 key 的成员
     */
    Boolean sIsMember(String key, Object member);

    /**
     * 8	SMEMBERS key
     * 返回集合中的所有成员
     */
    Set<Object> sMembers(String key);

    /**
     * 9	SMOVE source destination member
     * 将 member 元素从 source 集合移动到 destination 集合
     */
    Boolean sMove(String srcKey, String targetKey, Object member);

    /**
     * 10	SPOP key
     * 移除并返回集合中的一个随机元素
     */
    Object sPop(String key);


    Object sRandMember(String key);

    /**
     * 11	SRANDMEMBER key [count]
     * 返回集合中一个或多个随机数
     */
    List<Object> sRandMember(String key, long count);


    Long sRem(String key, Object member);

    /**
     * 12	SREM key member1 [member2]
     * 移除集合中一个或多个成员
     */
    Long sRem(String key, Collection<Object> members);


    Set<Object> sUnion(String key1, String key2);

    /**
     * 13	SUNION key1 [key2]
     * 返回所有给定集合的并集
     */
    Set<Object> sUnion(String key1, Collection<String> keys);

    /**
     * 14	SUNIONSTORE destination key1 [key2]
     * 所有给定集合的并集存储在 destination 集合中
     */
    Long sUnionStore(String key1, String key2, String targetKey);

    Long sUnionStore(String key1, Collection<String> key2, String targetKey);

    /**
     * 15	SSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代集合中的元素
     */
    void sScan(String key);

    /**
     * 1	ZADD key score1 member1 [score2 member2]
     * 向有序集合添加一个或多个成员，或者更新已存在成员的分数
     */
    Boolean zAdd(String key, Object member, double score);

    Long zAdd(String key, Map<Object, Double> members);

    /**
     * 2	ZCARD key
     * 获取有序集合的成员数
     */
    Long zCard(String key);

    /**
     * 3	ZCOUNT key min max
     * 计算在有序集合中指定区间分数的成员数
     */
    Long zCount(String key, double min, double max);

    /**
     * 4	ZINCRBY key increment member
     * 有序集合中对指定成员的分数加上增量 increment
     */
    Double zIncrBy(String key, double delta, Object member);
    Double zDecrBy(String key, double delta, Object member);

    /**
     * 5	ZINTERSTORE destination numkeys key [key ...]
     * 计算给定的一个或多个有序集的交集并将结果集存储在新的有序集合 key 中
     */
    Long zInterStore(String key1, String key2, String targetKey);

    Long zInterStore(String key1, Collection<String> key2, String targetKey);

    /**
     * 6	ZLEXCOUNT key min max
     * 在有序集合中计算指定字典区间内成员数量
     */
    Long zLexCount(String key, double min, double max);

    /**
     * 7	ZRANGE key start stop [WITHSCORES]
     * 通过索引区间返回有序集合成指定区间内的成员
     */
    Set<Object> zRange(String key, long start, long stop);

    /**
     * 8	ZRANGEBYLEX key min max [LIMIT offset count]
     * 通过字典区间返回有序集合的成员
     */
    Set<Object> zRangeByLex(String key, double min, double max);

    /**
     * 9	ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT]
     * 通过分数返回有序集合指定区间内的成员
     */
    Set<Object> zRangeByScore(String key, double min, double max);

    /**
     * 10	ZRANK key member
     * 返回有序集合中指定成员的索引
     */
    Long zRank(String key, Object member);

    /**
     * 11	ZREM key member [member ...]
     * 移除有序集合中的一个或多个成员
     */
    Long zRem(String key, Object member);

    Long zRem(String key, Collection<Object> members);

    /**
     * 14	ZREMRANGEBYSCORE key min max
     * 移除有序集合中给定的分数区间的所有成员
     */
    Long zRemRangeByScore(String key, double min, double max);

    /**
     * 13	ZREMRANGEBYRANK key start stop
     * 移除有序集合中给定的排名区间的所有成员
     */
    Long zRemRangeByRank(String key, long start, long stop);


    /**
     * 15	ZREVRANGE key start stop [WITHSCORES]
     * 返回有序集中指定区间内的成员，通过索引，分数从高到底
     */
    Set<Object> zRevRange(String key, long start, long stop);

    /**
     * 16	ZREVRANGEBYSCORE key max min [WITHSCORES]
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     */
    Set<Object> zRevRangeByScore(String key, double start, double stop);

    /**
     * 17	ZREVRANK key member
     * 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序
     */
    Long zRevRank(String key, Object member);

    /**
     * 18	ZSCORE key member
     * 返回有序集中，成员的分数值
     */
    Double zScore(String key, Object member);

    /**
     * 19	ZUNIONSTORE destination numkeys key [key ...]
     * 计算给定的一个或多个有序集的并集，并存储在新的 key 中
     */
    Long zUnionStore(String key1, String key2, String targetKey);

    Long zUnionStore(String key1, Collection<String> key2, String targetKey);

    /**
     * 20	ZSCAN key cursor [MATCH pattern] [COUNT count]
     * 迭代有序集合中的元素（包括元素成员和元素分值）
     */
    void zScan(String key);

    /**
     * 1	DEL key
     * 该命令用于在 key 存在时删除 key。
     */
    Boolean del(String key);

    Long del(Collection<String> key);


    /**
     * 3	EXISTS key
     * 检查给定 key 是否存在。
     */
    Boolean exists(String key);

    /**
     * 4	EXPIRE key seconds
     * 为给定 key 设置过期时间，以秒计。
     */
    Boolean expire(String key, long timeout);
    Boolean expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 5	EXPIREAT key timestamp
     * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。
     */
    Boolean expireAt(String key, Date date);
/**
 6	PEXPIRE key milliseconds
 设置 key 的过期时间以毫秒计。
 */
Boolean pExpire(String key, long timeout);

/**
 7	PEXPIREAT key milliseconds-timestamp
 设置 key 过期时间的时间戳(unix timestamp) 以毫秒计
 */
Boolean pExpireAt(String key, Date date);


    /**
     * 10	PERSIST key
     * 移除 key 的过期时间，key 将持久保持。
     */
    Boolean persist(String key);

    /**
     * 11	PTTL key
     * 以毫秒为单位返回 key 的剩余的过期时间。
     */
    Long pTtl(String key);

    /**
     * 12	TTL key
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     */
    Long ttl(String key);

    /**
     * 13	RANDOMKEY
     * 从当前数据库中随机返回一个 key 。
     */
    String randomKey();

    /**
     * 14	RENAME key newkey
     * 修改 key 的名称
     */

    void rename(String key, String newKey);

    /**
     * 15	RENAMENX key newkey
     * 仅当 newkey 不存在时，将 key 改名为 newkey 。
     */

    Boolean renameNx(String key, String newKey);

    /**
     * 16	TYPE key
     * 返回 key 所储存的值的类型。
     */
    DataType type(String key);


}
