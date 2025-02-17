package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;

public interface PackEventHelperServiceImpl {
	Page<PackEvent> findByPackId(Long id, Pageable pageable);
	Page<PackEventResponse> getPackEvents(Pageable pageable);
}
