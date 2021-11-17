package com.exchange.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    @Test
    void put_shouldAddDataToCacheForRetrieval_whenRequested() {
        Cache<String, String> lruCache = new CacheImpl<>(3);
        lruCache.put("1", "value1");
        lruCache.put("2", "value2");
        lruCache.put("3", "value3");

        assertEquals("value1", lruCache.get("1").get());
        assertEquals("value2", lruCache.get("2").get());
        assertEquals("value3", lruCache.get("3").get());
    }

    @Test
    void put_shouldRemoveOldestEntryAndAddNew_whenAddingNewValueToAFullCache() {
        Cache<String, String> lruCache = new CacheImpl<>(3);
        lruCache.put("1", "value1"); // oldest entry
        lruCache.put("2", "value2");
        lruCache.put("3", "value3");
        lruCache.put("4", "value4");

        assertEquals("value2", lruCache.get("2").get());
        assertEquals("value3", lruCache.get("3").get());
        assertEquals("value4", lruCache.get("4").get());
        assertTrue(lruCache.get("1").isEmpty());
    }

    @Test
    void put_shouldRearrangeTheOrderOfOldestEntriesRemoveOldestAndAddNew_whenAddingNewValueToAFullCacheAfterRead() {
        // given cache with stored entries
        Cache<String, String> lruCache = new CacheImpl<>(3);
        lruCache.put("1", "value1"); // oldest entry
        lruCache.put("2", "value2");
        lruCache.put("3", "value3");

        // when getting one value and then putting a new one
        assertEquals("value1", lruCache.get("1").get()); // 2 becomes and oldest entry
        lruCache.put("4", "value4");

        // then accessed entry order is updated and its not removed even being stored first
        assertEquals("value1", lruCache.get("1").get());
        assertEquals("value3", lruCache.get("3").get());
        assertEquals("value4", lruCache.get("4").get());
        assertTrue(lruCache.get("2").isEmpty());
    }

    @Test
    void get_shouldReturnEmptyValue_whenEmptyCache() {
        Cache<String, String> lruCache = new CacheImpl<>(3);
        assertTrue(lruCache.get("anyKey").isEmpty());
        assertTrue(lruCache.get("anyOtherKey").isEmpty());
    }

    @Test
    void clear_shouldEmptyTheCache_whenCalled() {
        // given cache with stored entries
        Cache<String, String> lruCache = new CacheImpl<>(3);
        lruCache.put("1", "value1"); // oldest entry
        lruCache.put("2", "value2");
        lruCache.put("3", "value3");
        // when clear is called
        lruCache.clear();
        // then values are no longer present
        assertTrue(lruCache.get("1").isEmpty());
        assertTrue(lruCache.get("2").isEmpty());
        assertTrue(lruCache.get("3").isEmpty());
    }
}
