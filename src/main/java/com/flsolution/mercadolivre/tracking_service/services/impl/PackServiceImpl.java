package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flsolution.mercadolivre.tracking_service.dtos.request.PackRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackCancelResponse;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerNotFoundException;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackCreateDuplicateDetectedException;

public interface PackServiceImpl {
	PackResponse createPack(PackRequest request) throws CustomerNotFoundException, PackCreateDuplicateDetectedException;
	PackResponse updateStatusPack(Long id, PackageStatus packageStatus);
	PackResponse getPackByIdAndIncludeEvents(Long id, Boolean includeEvents, Pageable pageable);
	
	Page<PackResponse> getPacks(String sender, String recipient, Pageable pageable);

	PackCancelResponse cancelPack(Long id) throws BadRequestException;
	
	Pack getPackById(Long id);
}
