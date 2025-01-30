package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;

import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

public interface PackServiceImpl {
	PackResponseDTO createPack(PackRequestDTO request);
	PackResponseDTO updateStatusPack(Long id, PackageStatus packageStatus);
	PackResponseDTO getPackByIdAndIncludeEvents(Long id, Boolean includeEvents, Pageable pageable);
	
	Page<PackResponseDTO> getPacks(String sender, String recipient, Pageable pageable);

	PackCancelResponseDTO cancelPack(Long id) throws BadRequestException;
	
	Pack getPackById(Long id);
	
	CacheControl getCacheControl();

}
