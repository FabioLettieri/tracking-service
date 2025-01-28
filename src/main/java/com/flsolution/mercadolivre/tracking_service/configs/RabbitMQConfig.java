package com.flsolution.mercadolivre.tracking_service.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);
	public static final String REQUEST_LOG_QUEUE = "request-log-queue";
	
	@Bean
    Queue requestLogQueue() {
        return new Queue(REQUEST_LOG_QUEUE, true);
    }
	
	@Bean
	Jackson2JsonMessageConverter jsonMessageConverter() {
		logger.info("[START] - jsonMessageConverter()");
		Jackson2JsonMessageConverter response = new Jackson2JsonMessageConverter();
		logger.info("[FINISH] - jsonMessageConverter()");
        
		return response;
    }
	
	@Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
		logger.info("[START] - rabbitTemplate() messageConverter: {}", messageConverter);
		
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        
        logger.info("[FINISH] - messageConverter()");

        return rabbitTemplate;
    }
	
}
