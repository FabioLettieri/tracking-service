package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;
import com.flsolution.mercadolivre.tracking_service.repositories.RequestLogRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.RequestLogServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestLogService implements RequestLogServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestLogService.class);
	private final RequestLogRepository requestLogRepository;

	@Override
	public RequestLog createLogRequest(RequestLog requestLog) throws Exception {
		try {
			logger.info("[START] - createLogRequest()");
			
			RequestLog response = requestLogRepository.save(requestLog);
			
			logger.info("[FINISH] - createLogRequest()");
			return response;
		} catch (Exception ex) {
			logger.info("[FINISH] - createLogRequest() WITH ERRORS: {}", ex.getMessage());
			throw new Exception();
		}
		
		
	}

}
