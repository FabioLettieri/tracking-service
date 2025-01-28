package com.flsolution.mercadolivre.tracking_service.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flsolution.mercadolivre.tracking_service.configs.RabbitMQConfig;
import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;
import com.flsolution.mercadolivre.tracking_service.services.RequestLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestLogConsumer {

	private static final Logger logger = LoggerFactory.getLogger(RequestLogConsumer.class);
	
	private final RequestLogService requestLogServiceImpl;
	private final ObjectMapper objectMapper;
	
	@RabbitListener(queues = RabbitMQConfig.REQUEST_LOG_QUEUE)
	public void consumeRequestLog(String message) {
		try {
			logger.info("[START] - consumeRequestLog() message: {}", message);

			RequestLog requestLog = objectMapper.readValue(message, RequestLog.class);
			requestLogServiceImpl.createLogRequest(requestLog);
			
			logger.info("[FINISH] - consumeRequestLog()");
		} catch (Exception ex) {
			logger.error("[FINSH] - consumeRequestLog() WITH ERRORS: {}", ex.getMessage(), ex);
		}
	}
	
}
