package com.flsolution.mercadolivre.tracking_service.services;

import java.time.LocalDateTime;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.flsolution.mercadolivre.tracking_service.converters.PackConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.repositories.PackRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackServiceImpl;
import com.flsolution.mercadolivre.tracking_service.utils.PackValidation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackService implements PackServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(PackService.class);
	
	private final PackRepository packRepository;
	private final PackEventHelperService packEventHelperService;
	private final ExternalApiNagerService apiNagerService;
	private final ExternalApiTheDogService apiTheDogService;
	
	@Override
	public PackResponseDTO createPack(PackRequestDTO request) {
		logger.info("[START] - createPack() request: {}", request);
		
		Boolean isHolliday = apiNagerService.isHoliday(request.getEstimatedDeliveryDate());
		request.setIsHolliday(isHolliday);
		
		String funFact = apiTheDogService.getFunFact();
		request.setFunfact(funFact);
		
		Pack savedPack = packRepository.save(PackConverter.toEntity(request));

		PackResponseDTO response = PackConverter.toResponseDTO(savedPack);
		
		logger.info("[FINISH] - createPack()");
		return response;
	}

	@Override
	public PackResponseDTO updateStatusPack(Long id, PackageStatus packageStatus) {
		logger.info("[START] - updateStatusPack() id: {}, packageStatus: {}", id, packageStatus);

		Pack pack = getPackById(id);
		
		PackValidation.validateStatusTransition(pack.getStatus(), packageStatus);
		pack.setStatus(packageStatus);
		
		if (packageStatus == PackageStatus.DELIVERED) {
			pack.setDeliveredAt(LocalDateTime.now());
		}
		
		Pack updatedPack = packRepository.save(pack);
		PackResponseDTO response = PackConverter.toResponseDTO(updatedPack);
		
		logger.info("[FINISH] - updateStatusPack()");
		return response;
	}


	@Override
	public PackResponseDTO getPackByIdAndIncludeEvents(Long id, Boolean includeEvents, Pageable pageable) {
		logger.info("[START] - getPackById() id: {}, includeEvents: {}", id, includeEvents);
		
		Pack pack = getPackById(id);
		
		Page<PackEvent> events = includeEvents ? packEventHelperService.findByPackId(id, pageable) : Page.empty();
		
		PackResponseDTO response = PackConverter.listEventToResponseDTO(pack, events);
		
		logger.info("[FINISH] - getPackById()");
		return response;
	}

	@Override
	public Pack getPackById(Long id) {
		logger.info("[START] - getPackById() id: {}", id);
		
		Pack response = packRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pack not found."));

		logger.info("[FINISH] - getPackById()");
		return response;
	}

	@Override
	public PackCancelResponseDTO cancelPack(Long id) throws BadRequestException {
		logger.info("[START] - cencelPack() id: {}", id);

		Pack pack = getPackById(id);
		
		PackValidation.validatePackElegibleForCancellation(pack.getStatus());
		
		pack.setStatus(PackageStatus.CANCELLED);
		PackCancelResponseDTO response = PackConverter.toCancelResponseDTO(packRepository.save(pack));
		
		logger.info("[FINISH] - cencelPack()");
		return response;
	}

	@Override
	public Page<PackResponseDTO> getPacks(Pageable pageable) {
		logger.info("[START] - getPacks() pageable: {}", pageable);
		Page<Pack> packs = packRepository.findAll(pageable);
		
		Page<PackResponseDTO> response = PackConverter.toListPackResponseDTO(packs);
		
		logger.info("[FINISH] - getPacks() total elements: {}, total pages: {}", response.getTotalElements(), response.getTotalPages());
		return response;
	}
	
}
