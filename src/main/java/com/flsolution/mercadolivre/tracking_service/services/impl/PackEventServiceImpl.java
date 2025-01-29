package com.flsolution.mercadolivre.tracking_service.services.impl;

import java.util.List;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.services.PackEventRequestDTO;

public interface PackEventServiceImpl {
	PackEventDTO createPackEvent(PackEventRequestDTO requestDTO);
	List<PackEvent> findByPackId(Long id);
	List<PackEventDTO> getPackEvents(String sender, String recipient);
	

}
