package com.exchange.cache;

import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheImpl<K, V> implements Cache<K, V> {
    private final Map<K, V> storageMap;
    private final Deque<K> linkedList;
    private final int capacity;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public CacheImpl(int capacity) {
        this.linkedList = new LinkedBlockingDeque<>();
        this.storageMap = new ConcurrentHashMap<>(capacity);
        this.capacity = capacity;
    }

    @Override
    public Optional<V> get(K key) {
        this.lock.readLock().lock();
        try {
            if (!storageMap.containsKey(key)) {
                return Optional.empty();
            }
            moveElementToFront(key);
            return Optional.ofNullable(storageMap.get(key));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        this.lock.writeLock().lock();
        try {
            if (storageMap.containsKey(key)) {
                moveElementToFront(key);
            } else {
                if (capacity == storageMap.size()) {
                    evictCachedValue();
                }
                linkedList.push(key);
            }
            storageMap.put(key, value);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        this.lock.writeLock().lock();
        try {
            linkedList.clear();
            storageMap.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private synchronized void moveElementToFront(K key) {
        linkedList.remove(key);
        linkedList.push(key);
    }

    private void evictCachedValue() {
        this.lock.writeLock().lock();
        try {
            var key = linkedList.removeLast();
            storageMap.remove(key);
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
