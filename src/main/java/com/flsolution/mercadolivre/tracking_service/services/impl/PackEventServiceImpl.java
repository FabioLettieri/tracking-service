package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;

import com.flsolution.mercadolivre.tracking_service.dtos.request.PackEventRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;

public interface PackEventServiceImpl {
	PackEventResponse createPackEvent(PackEventRequest requestDTO);
	Page<PackEventResponse> getPackEvents(Pageable pageable);
	Page<PackEvent> findByPackId(Long id, Pageable pageable);
	CacheControl getCacheControl();
	

}
