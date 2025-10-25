package org.codebook.multilevelcachesystem.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisCacheConfig {

    @Bean
    public Cache<String,String> redisCache(RedisTemplate<String,String> redisTemplate) {
        return new RedisCache<>(redisTemplate);
    }
}
