package com.flsolution.mercadolivre.tracking_service.configs;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

	@Bean(name = "taskExecutor")
	Executor taskExecutor() {
		logger.info("[START] - taskExecutor()");
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
		
		logger.info("[FINISH] - taskExecutor()");
		return executor;
	}
	
}
