package com.wdcloud.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 赵秀非
 */
@SuppressWarnings({"JavaDoc", "SpringJavaAutowiredFieldsWarningInspection"})
@Configuration
public class WdRedisAutoConfiguration {
    @Bean
    public IRedisService redisService(RedisTemplate<String, String> stringRedisTemplate) {
        return new RedisService(stringRedisTemplate);
    }
}
