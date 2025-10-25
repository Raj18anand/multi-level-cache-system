package org.codebook.multilevelcachesystem.cache;

public class InMemoryCache<K, V> implements Cache<K, V> {
    private final Cache<K, V> cache;

    public InMemoryCache(int capacity, EvictionPolicy policy){
        this.cache = switch(policy){
            case LRU -> new LruCache<>(capacity);
            case LFU -> new LfuCache<>(capacity);
        };
    }

    public V get(K key){
        return cache.get(key);
    }

    public void set(K key, V value, long ttlMillis){
        cache.set(key, value, ttlMillis);
    }

    public void remove(K key){
        cache.remove(key);
    }
}
