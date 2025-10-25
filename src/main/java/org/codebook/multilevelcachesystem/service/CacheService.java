package org.codebook.multilevelcachesystem.service;

import org.codebook.multilevelcachesystem.cache.Cache;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private final Cache<String,String> inMemoryCache;

    public CacheService(Cache<String,String> inMemoryCache){
        this.inMemoryCache=inMemoryCache;
    }

    public String get(String key){
        return inMemoryCache.get(key);
    }

    public void set(String key, String value){
        inMemoryCache.set(key, value, 0);
    }

    public void set(String key, String value, long ttlMillis){
        inMemoryCache.set(key, value, ttlMillis);
    }

    public void remove(String key){
        inMemoryCache.remove(key);
    }
}
