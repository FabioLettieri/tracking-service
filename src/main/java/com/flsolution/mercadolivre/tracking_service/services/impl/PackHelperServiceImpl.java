package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flsolution.mercadolivre.tracking_service.dtos.response.PackResponse;

public interface PackHelperServiceImpl {
	Page<PackResponse> getPackEvents(String sender, String recipient, Pageable pageable);

}
