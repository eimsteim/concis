/**
 * Copyright 2022-2025 Leo (https://github.com/eimsteim/concis)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joycoho.concis.kernel.cache.ehcache;

import com.joycoho.concis.kernel.context.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.List;

/**
 * 缓存工具类
 */
@Slf4j
public class EhCacheUtil {

    private static final Object LOCKER = new Object();

    /**
     * 可自定义过期时间的PUT
     * @param cacheName
     * @param key
     * @param value
     * @param liveSeconds
     */
    public static void put(String cacheName, Object key, Object value, int liveSeconds) {
        Element el = new Element(key, value);
        el.setEternal(false);
        el.setTimeToIdle(0);
        el.setTimeToLive(liveSeconds);
        getOrAddCache(cacheName).put(el);
    }

    /**
     * 使用ehcache.xml配置中的默认过期时间
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, Object key, Object value) {
        getOrAddCache(cacheName).put(new Element(key, value));
    }

    @SuppressWarnings("all")
    public static <T> T get(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        if (element == null) {
            return null;
        } else {
            Object objectValue = element.getObjectValue();
            return (T) objectValue;
        }
    }

    public static List getKeys(String cacheName) {
        return getOrAddCache(cacheName).getKeys();
    }

    public static void remove(String cacheName, Object key) {
        getOrAddCache(cacheName).remove(key);
    }

    public static void removeAll(String cacheName) {
        getOrAddCache(cacheName).removeAll();
    }

    private static Cache getOrAddCache(String cacheName) {
        CacheManager cacheManager = SpringContextHolder.getBean(CacheManager.class);
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            synchronized (LOCKER) {
                cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    cacheManager.addCacheIfAbsent(cacheName);
                    cache = cacheManager.getCache(cacheName);
                }
            }
        }
        return cache;
    }
}


