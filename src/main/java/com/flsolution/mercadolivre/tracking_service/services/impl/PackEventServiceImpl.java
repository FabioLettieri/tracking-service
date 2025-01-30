package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;

public interface PackEventServiceImpl {
	PackEventDTO createPackEvent(PackEventRequestDTO requestDTO);
	Page<PackEventDTO> getPackEvents(Pageable pageable);
	Page<PackEvent> findByPackId(Long id, Pageable pageable);
	CacheControl getCacheControl();
	

}
