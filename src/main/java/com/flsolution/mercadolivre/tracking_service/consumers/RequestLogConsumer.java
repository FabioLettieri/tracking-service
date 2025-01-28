package com.flsolution.mercadolivre.tracking_service.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.flsolution.mercadolivre.tracking_service.configs.RabbitMQConfig;
import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;
import com.flsolution.mercadolivre.tracking_service.repositories.RequestLogRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestLogConsumer {

	private final RequestLogRepository requestLogRepository;
	
	@RabbitListener(queues = RabbitMQConfig.REQUEST_LOG_QUEUE)
	public void consumeRequestLog(RequestLog requestLog) {
		requestLogRepository.save(requestLog);
	}
	
}
