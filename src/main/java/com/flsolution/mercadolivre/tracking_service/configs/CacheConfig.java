package com.flsolution.mercadolivre.tracking_service.configs;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

    @Bean
    CacheManager cacheManager() {
    	logger.info("[START] - cacheManager()");
    	
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("packs", "packsById", "packsIncludeEvents");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(50) 
                .expireAfterWrite(1, TimeUnit.MINUTES) 
        );
        
        logger.info("[FINISH] - cacheManager()");
        return cacheManager;
    }
}
