package com.flsolution.mercadolivre.tracking_service.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.flsolution.mercadolivre.tracking_service.configs.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestLogProducer {

	private final RabbitTemplate rabbitTemplate;
	
	public void sendRequestLog(String logMessage) {
		rabbitTemplate.convertAndSend(RabbitMQConfig.REQUEST_LOG_QUEUE, logMessage);
	}
	
}
