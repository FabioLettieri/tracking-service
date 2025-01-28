package com.flsolution.mercadolivre.tracking_service.producers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.flsolution.mercadolivre.tracking_service.configs.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestLogProducer {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestLogProducer.class);
	
	private final RabbitTemplate rabbitTemplate;
	
	public void sendMessage(String message) throws Exception {
		try {
			logger.info("[START] - sendMessage() - message: {}", message);
			
			rabbitTemplate.convertAndSend(RabbitMQConfig.REQUEST_LOG_QUEUE, message);
			
			logger.info("[FINISH] - sendMessage()");
		} catch (Exception ex) {
			logger.info("[FINISH] - sendMessage() WITH ERRORS: {}", ex.getMessage());
			throw new Exception();
		}
    }
	
}
