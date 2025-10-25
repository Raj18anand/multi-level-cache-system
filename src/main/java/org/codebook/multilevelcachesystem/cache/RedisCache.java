package org.codebook.multilevelcachesystem.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisCache<K,V> implements Cache<K,V> {
    private final RedisTemplate<K,V> redisTemplate;
    public RedisCache(RedisTemplate<K,V> redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void set(K key, V value, long ttlMillis) {
        if(ttlMillis>0){
            redisTemplate.opsForValue().set(key,value,ttlMillis, TimeUnit.MILLISECONDS);
            return;
        }
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void remove(K key) {
        redisTemplate.delete(key);
    }
}
