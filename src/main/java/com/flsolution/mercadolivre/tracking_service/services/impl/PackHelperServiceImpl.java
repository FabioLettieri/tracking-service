package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;

public interface PackHelperServiceImpl {
	Page<PackResponseDTO> getPackEvents(String sender, String recipient, Pageable pageable);

}
