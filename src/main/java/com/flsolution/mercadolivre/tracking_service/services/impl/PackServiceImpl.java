package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.apache.coyote.BadRequestException;

import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

public interface PackServiceImpl {
	PackResponseDTO createPack(PackRequestDTO request);
	PackResponseDTO updateStatusPack(Long id, PackageStatus packageStatus);
	PackResponseDTO getPackByIdAndIncludeEvents(Long id, Boolean includeEvents);
	PackCancelResponseDTO cancelPack(Long id) throws BadRequestException;
	
	Pack getPackById(Long id);

}
