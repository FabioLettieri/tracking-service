package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.PackConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackService implements PackServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(PackService.class);
	
	private final PackRepository packRepository;
	private final ExternalApiNagerService apiNagerService;
	private final ExternalApiTheDogService apiTheDogService;
	
	@Override
	public PackResponseDTO createPack(PackRequestDTO request) {
		logger.info("[START] - createPack() request: {}", request);
		
		Boolean isHolliday = apiNagerService.isHolliday(request.getEstimatedDeliveryDate());
		request.setIsHolliday(isHolliday);
		
		String funFact = apiTheDogService.getFunFact();
		request.setFunfact(funFact);
		
		Pack savedPack = packRepository.save(PackConverter.toEntity(request));

		PackResponseDTO response = PackConverter.toResponseDTO(savedPack);
		
		logger.info("[FINISH] - createPack()");
		
		return response;
	}

}
