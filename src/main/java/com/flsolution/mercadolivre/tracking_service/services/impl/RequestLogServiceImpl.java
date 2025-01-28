package com.flsolution.mercadolivre.tracking_service.services.impl;

import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;

public interface RequestLogServiceImpl {
	RequestLog createLogRequest(RequestLog requestLog) throws Exception;

}
