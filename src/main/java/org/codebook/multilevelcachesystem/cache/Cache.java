package org.codebook.multilevelcachesystem.cache;

public interface Cache<K, V> {
    V get(K key);
    void set(K key, V value, long ttlMillis);
    void remove(K key);
    default void set(K key, V value){
        set(key, value, 0);
    }
}
