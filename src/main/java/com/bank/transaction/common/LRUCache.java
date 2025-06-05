package com.bank.transaction.common;


import java.util.LinkedHashMap;
import java.util.Map;


/**
 * LRU cache
 */
public class LRUCache<K, V> {
    private final int cacheCapacity;
    private final LinkedHashMap<K, V> cache;

    public LRUCache(int capacity) {
        this.cache = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > cacheCapacity;
            }
        };
        this.cacheCapacity = capacity;
    }

    public synchronized V get(K key) {
        return cache.get(key);
    }

    public synchronized V put(K key, V value) {
        return cache.put(key, value);
    }

    public synchronized V remove(K key) {
        return cache.remove(key);
    }
}

