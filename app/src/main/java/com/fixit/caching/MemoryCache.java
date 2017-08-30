package com.fixit.caching;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Kostyantin on 8/24/2017.
 */

public class MemoryCache<K, V> {

    public static final int LOW_PRIORITY = 0;

    public static final int MEDIUM_PRIORITY = 1;

    public static final int HIGH_PRIORITY = 2;

    private final ConcurrentMap<CacheKey<K>, V> mCache = new ConcurrentSkipListMap<>(new Comparator<CacheKey<K>>() {
        @Override
        public int compare(CacheKey<K> o1, CacheKey<K> o2) {
            return o1.compareTo(o2);
        }
    });

    private final int maxItems;
    private final Object mLock = new Object();

    public MemoryCache(int maxItems) {
        if(maxItems < 0) {
            throw new IllegalArgumentException("maxItems must be a positive number starting from 1.");
        }
        this.maxItems = maxItems;
    }

    public void put(K key, V value) {
        put(key, value, LOW_PRIORITY);
    }

    public void put(K key, V value, int priority) {
      //  checkMemory();
        mCache.put(new CacheKey<K>(key, priority), value);
    }

    private void checkMemory() {
        synchronized (mLock) {
            if (mCache.size() > maxItems) {
                int toRemove = maxItems / 7;
                Iterator<CacheKey<K>> iterator = mCache.keySet().iterator();
                for (int i = 0; i < toRemove; i++) {
                    CacheKey<K> key = iterator.next();
                    mCache.remove(key);
                }
            }
        }
    }

    public static class CacheKey<K> implements Comparable<CacheKey<K>> {
        final K key;
        final long createdAt;
        final int priority;

        public CacheKey(K item, int priority) {
            this.key = item;
            this.createdAt = System.currentTimeMillis();
            this.priority = priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey<?> cacheKey = (CacheKey<?>) o;
            return key.equals(cacheKey.key);

        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public int compareTo(@NonNull CacheKey<K> o) {
            if(priority == o.priority) {
                return ((int) (createdAt - o.createdAt));
            } else {
                return priority - o.priority;
            }
        }
    }

}
