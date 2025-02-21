package com.flsolution.mercadolivre.tracking_service.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.ResponseEntity;

import io.github.resilience4j.retry.annotation.Retry;

@Configuration
public class RedisConfig {
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean
	RedisConnectionFactory redisConnectionFactory() {
		logger.info("[START] - redisConnectionFactory()");
		
		var lettuceConnectionFactory = new LettuceConnectionFactory();
		lettuceConnectionFactory.setValidateConnection(true);
		
		logger.info("[FINISH] - redisConnectionFactory()");
		return lettuceConnectionFactory;
	}
	
	@Bean
	RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		logger.info("[START] - redisTemplate() redisConnectionFactory: {}", redisConnectionFactory);

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		
		logger.info("[FINISH] - redisTemplate()");
		return template;
	}
	
	@Retry(name = "redisRetry", fallbackMethod = "fallbackRedisOperation")
	public Object performRedisOperation(RedisTemplate<String, Object> redisTemplate, String key) {
		logger.info("[START] - performRedisOperation() redisTemplate: {}, key: {}", redisTemplate, key);
		var response = redisTemplate.opsForValue().get(key);
		
		logger.info("[FINISH] - performRedisOperation()");
		return response;
	}
	
	public ResponseEntity<String> fallbackRedisOperation(Exception ex) {
        logger.error("[FALLBACK] - Falha ao acessar Redis: {}", ex.getMessage());
        return ResponseEntity.internalServerError().body("Operation Redis error."); 
    }
	

}
