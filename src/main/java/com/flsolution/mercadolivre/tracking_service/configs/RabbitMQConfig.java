package com.flsolution.mercadolivre.tracking_service.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

	public static final String REQUEST_LOG_QUEUE = "request-log-queue";
	
	@Bean
	Queue requestLogQueue() {
		return new Queue(REQUEST_LOG_QUEUE, true);
	}
	
}
