package com.flsolution.mercadolivre.tracking_service.services.impl;

import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;

public interface PackServiceImpl {
	PackResponseDTO createPack(PackRequestDTO request);

}
