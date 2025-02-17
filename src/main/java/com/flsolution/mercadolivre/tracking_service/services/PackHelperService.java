package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.PackConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackHelperServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackHelperService implements PackHelperServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(PackHelperService.class);
	
	private final PackRepository packRepository;

	@Override
	public Page<PackResponse> getPackEvents(String sender, String recipient, Pageable pageable) {
		logger.info("[START] - getPackEvents() sender: {}, recipient: {}", sender, recipient);

		Page<Pack> events;

		if (sender != null && recipient != null) {
		    events = packRepository.findBySenderAndRecipient(sender, recipient, pageable);
		} else if (sender != null) {
		    events = packRepository.findBySender(sender, pageable);
		} else if (recipient != null) {
		    events = packRepository.findByRecipient(recipient, pageable);
		} else {
		    events = packRepository.findAll(pageable);
		}

		Page<PackResponse> response = events.map(PackConverter::toResponseDTO);

		logger.info("[FINISH] - getPackEvents() total elements: {}, total pages: {}", response.getTotalElements(), response.getTotalPages());
		return response;
	}


}
