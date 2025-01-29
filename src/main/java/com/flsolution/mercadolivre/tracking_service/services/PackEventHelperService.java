package com.flsolution.mercadolivre.tracking_service.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.PackEventConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
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
	public List<PackEvent> findByPackId(Long id) {
		logger.info("[START] - findPackById() id: {}", id);
		
		List<PackEvent> eventPacks = packEventRepository.findByPackId(id);

		logger.info("[FINISH] - findPackById()");
		
		return eventPacks;
	}

	@Override
	public List<PackEventDTO> getPackEvents(String sender, String recipient) {
	    logger.info("[START] - getPackEvents() sender: {}, recipient: {}", sender, recipient);
		
		List<PackEvent> events;
		
		if (sender != null && recipient != null) {
		    events = packEventRepository.findBySenderAndRecipient(sender, recipient);
		} else if (sender != null) {
		    events = packEventRepository.findBySender(sender);
		} else if (recipient != null) {
		    events = packEventRepository.findByRecipient(recipient);
		} else {
		    events = packEventRepository.findAll();
		}
		
		List<PackEventDTO> response = events.stream()
		        .map(PackEventConverter::toDTO)
		        .toList();
		
		logger.info("[FINISH] - getPackEvents() total found: {}", response.size());
		return response;
	}

}
