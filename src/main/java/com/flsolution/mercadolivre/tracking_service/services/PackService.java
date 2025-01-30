package com.flsolution.mercadolivre.tracking_service.services;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.PackConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.PackNotFoundException;
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
	private final PackHelperService packHelperService;
	
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
	@Cacheable(value = "packsById", key = "#id")
	public Pack getPackById(Long id) {
		logger.info("[START] - getPackById() id: {}", id);
		
		Pack response = packRepository.findById(id)
				.orElseThrow(() -> new PackNotFoundException("Pack not found with id: " + id));

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
	@Cacheable(value = "packs", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
	public Page<PackResponseDTO> getPacks(String sender, String recipient, Pageable pageable) {
		logger.info("[START] - getPacks() pageable: {}", pageable);
		Page<PackResponseDTO> response = packHelperService.getPackEvents(sender, recipient, pageable);
		
		logger.info("[FINISH] - getPacks() total elements: {}, total pages: {}", response.getTotalElements(), response.getTotalPages());
		return response;
	}
	
	@Override
	@Cacheable(value = "packsIncludeEvents", key = "#id + '-' + #includeEvents")
    public PackResponseDTO getPackByIdAndIncludeEvents(Long id, Boolean includeEvents, Pageable pageable) {
        Pack pack = getPackById(id);

        Page<PackEvent> events = includeEvents ? packEventHelperService.findByPackId(id, pageable) : Page.empty();
        
        PackResponseDTO response = PackConverter.listEventToResponseDTO(pack, events);

        return response;
    }

	@Override
    public CacheControl getCacheControl() {
        return CacheControl.maxAge(5, TimeUnit.MINUTES);
    }
    
}
