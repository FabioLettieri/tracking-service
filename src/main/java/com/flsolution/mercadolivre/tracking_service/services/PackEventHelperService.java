package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.PackEventConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.repositories.PackEventRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventHelperServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackEventHelperService implements PackEventHelperServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(PackEventHelperService.class);
	
	private final PackEventRepository packEventRepository;

	@Override
	public Page<PackEvent> findByPackId(Long id, Pageable pageable) {
		logger.info("[START] - findPackById() id: {}", id);
		
		Page<PackEvent> eventPacks = packEventRepository.findByPackId(id, pageable);

		logger.info("[FINISH] - findPackById()");
		return eventPacks;
	}

	@Override
	public Page<PackEventResponse> getPackEvents(Pageable pageable) {
	    logger.info("[START] - getPackEvents() pageable: {}", pageable);
		
		Page<PackEvent> events = packEventRepository.findAll(pageable);
		
		Page<PackEventResponse> response = events.map(PackEventConverter::toDTO);
		
		logger.info("[FINISH] - getPackEvents() total found: {}, total pages: {}", response.getTotalElements(), response.getTotalPages());
		return response;
	}

}
