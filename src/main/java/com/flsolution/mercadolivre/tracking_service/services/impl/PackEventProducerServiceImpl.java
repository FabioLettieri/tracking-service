package com.flsolution.mercadolivre.tracking_service.services.impl;

import java.util.List;

import com.flsolution.mercadolivre.tracking_service.dtos.request.PackEventRequest;

public interface PackEventProducerServiceImpl {
	String sendPackEvent(PackEventRequest requestDTO) throws Exception;
	String sendListPackEvent(List<PackEventRequest> requestDTO);
}
