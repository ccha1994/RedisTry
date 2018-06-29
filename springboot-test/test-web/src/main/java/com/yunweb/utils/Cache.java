package com.yunweb.utils;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangyunlong
 * @date 2018/6/29 11:27
 */
@Component("cache")
public class Cache {
    /**
     * ConcurrentHashMap实现简单缓存.
     * ConcurrentHashMap的key和value均不为null.
     */
    private Map<String, Object> cache = new ConcurrentHashMap<>();

    public Object get(String key) {
        return cache.get(key);
    }

    public void put(String key, Object obj) {
        if(null != key && null != obj) {
            cache.put(key, obj);
        }
    }

    public void evict(String key) {
        if (null != key) {
            cache.remove(key);
        }
    }

    public String listAll() {
        return cache.toString();
    }
}
