package com.shanjupay.transaction.config;

import com.shanjupay.common.cache.Cache;
import com.shanjupay.transaction.common.util.RedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Classname RedisConfig
 * @Description redis 的配置类
 * @Date 2020/5/13 22:32
 * @Created by 小二哥
 */
@Configuration
public class RedisConfig {
    @Bean
    public Cache cache(StringRedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }
}
    