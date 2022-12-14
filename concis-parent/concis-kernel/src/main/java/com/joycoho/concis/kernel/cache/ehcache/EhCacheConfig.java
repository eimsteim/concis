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

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

/**
 * ehcache配置
 *
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "concis.cache", name = "type", havingValue = "ehcache")
public class EhCacheConfig {

    /**
     * EhCache的配置
     */
    @Bean
    @DependsOn("springContextHolder")
    public EhCacheCacheManager cacheManager(CacheManager cacheManager) {
        log.info("开始初始化 EhCacheCacheManager");
        return new EhCacheCacheManager(cacheManager);
    }

    /**
     * EhCache的配置
     */
    @Bean
    @DependsOn("springContextHolder")
    public EhCacheManagerFactoryBean ehcache() {
        log.info("开始初始化 EhCacheManagerFactoryBean");
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return ehCacheManagerFactoryBean;
    }
}
