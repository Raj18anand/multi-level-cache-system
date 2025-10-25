package org.codebook.multilevelcachesystem.service;

import org.codebook.multilevelcachesystem.cache.Cache;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private final Cache<String,String> inMemoryCache;
    private final Cache<String,String> redisCache;

    public CacheService(
            Cache<String,String> inMemoryCache,
            Cache<String,String> redisCache){
        this.inMemoryCache=inMemoryCache;
        this.redisCache=redisCache;
    }

    public String get(String key){
        String value=inMemoryCache.get(key);
        if(value!=null) return value;

        value=redisCache.get(key);
        if(value!=null){
            inMemoryCache.set(key,value);
            return value;
        }

        value="From DB";
        redisCache.set(key,value);
        inMemoryCache.set(key,value);
        return value;
    }

    public void set(String key, String value){
        inMemoryCache.set(key, value, 0);
        redisCache.set(key, value, 0);
    }

    public void set(String key, String value, long ttlMillis){
        inMemoryCache.set(key, value, ttlMillis);
        redisCache.set(key, value, ttlMillis);
    }

    public void remove(String key){
        inMemoryCache.remove(key);
        redisCache.remove(key);
    }
}
