package org.codebook.multilevelcachesystem.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.TreeMap;

public class LfuCache<K,V> implements Cache<K,V> {
    private final int capacity;

    private final HashMap<K, Node<K,V>> map;

    private final TreeMap<Integer, LinkedHashSet<K>> freqMap;

    public LfuCache(int capacity){
        this.capacity=capacity;
        this.map=new HashMap<>();
        this.freqMap=new TreeMap<>();
    }

    public synchronized V get(K key){
        if(!map.containsKey(key)){
            return null;
        }
        Node<K,V> node=map.get(key);
        if(isExpired(node)){
            map.remove(node.key);
            freqMap.get(node.frequency).remove(node.key);
            if(freqMap.get(node.frequency).isEmpty()){
                freqMap.remove(node.frequency);
            }
            return null;
        }
        increaseFrequency(node);
        return node.value;
    }

    public synchronized void set(K key, V value, long ttlMills){
        long expirationTime = ttlMills>0 ? System.currentTimeMillis()+ttlMills : 0;
        if(map.containsKey(key)){
            Node<K,V> existing = map.get(key);
            existing.value=value;
            existing.expirationTime=expirationTime;
            increaseFrequency(existing);
        } else {
            if(map.size()>=capacity){
                LinkedHashSet<K> minFrequencyKeys=freqMap.firstEntry().getValue();
                K removedKey=minFrequencyKeys.iterator().next();
                minFrequencyKeys.remove(removedKey);
                if(minFrequencyKeys.isEmpty()){
                    freqMap.remove(freqMap.firstKey());
                }
                map.remove(removedKey);
            }
            Node<K,V> newNode = new Node<>(key, value,1, expirationTime);
            freqMap.computeIfAbsent(1,k -> new LinkedHashSet<>()).add(key);
            map.put(key,newNode);
        }
    }

    public synchronized void remove(K key){
        if(map.containsKey(key)){
            Node<K,V> node = map.get(key);
            freqMap.get(node.frequency).remove(key);
            if(freqMap.get(node.frequency).isEmpty()){
                freqMap.remove(node.frequency);
            }
            map.remove(key);
        }
    }

    private void increaseFrequency(Node<K,V> node){
        int freq=node.frequency;
        freqMap.get(freq).remove(node.key);
        if(freqMap.get(freq).isEmpty()){
            freqMap.remove(freq);
        }
        node.frequency++;
        freqMap.computeIfAbsent(node.frequency,k -> new LinkedHashSet<>()).add(node.key);
    }

    private boolean isExpired(Node<K,V> node){
        return node.expirationTime>0 && node.expirationTime < System.currentTimeMillis();
    }

    // Node in a cache list
    private static class Node<K,V>{
        K key;
        V value;
        int frequency;
        long expirationTime; // 0 means no expiration
        Node(K key, V value, int frequency, long expirationTime){
            this.key = key;
            this.value = value;
            this.frequency = frequency;
            this.expirationTime = expirationTime;
        }
    }
}
