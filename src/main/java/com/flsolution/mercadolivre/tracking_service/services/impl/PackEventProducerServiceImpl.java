package com.flsolution.mercadolivre.tracking_service.services.impl;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;

public interface PackEventProducerServiceImpl {
	String sendPackEvent(PackEventRequestDTO requestDTO) throws Exception;
}
