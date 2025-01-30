package com.flsolution.mercadolivre.tracking_service.configs;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("packs", "packsById", "packsIncludeEvents");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(50) 
                .expireAfterWrite(1, TimeUnit.MINUTES) 
        );
        
        return cacheManager;
    }
}
