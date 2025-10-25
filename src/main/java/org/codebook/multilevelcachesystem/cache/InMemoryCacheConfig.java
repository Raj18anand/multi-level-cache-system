package org.codebook.multilevelcachesystem.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemoryCacheConfig {
    @Value("${cache.inMemory.capacity:1000}")
    private int capacity;

    @Value("${cache.inMemory.evictionPolicy:LRU}")
    private String evictionPolicy;

    @Bean
    public Cache<String,String> inMemoryCache() {
        EvictionPolicy policy = EvictionPolicy.valueOf(evictionPolicy);
        return new InMemoryCache<>(capacity,policy);
    }
}
