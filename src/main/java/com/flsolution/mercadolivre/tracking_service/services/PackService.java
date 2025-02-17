 package com.flsolution.mercadolivre.tracking_service.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.PackConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Customer;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerNotFoundException;
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
	private final CustomerService customerRepository;

	@Override
	@CachePut(value = "packsById", key = "#result.id")
	public PackResponseDTO createPack(PackRequestDTO request) throws CustomerNotFoundException {
		logger.info("[START] - createPack() request: {}", request);
		
	    Optional<Pack> existingPack = packRepository.findByCustomerId(request.getCustomerId());
	    if (existingPack.isPresent()) {
	        logger.warn("Duplicate request detected. Discarding duplicate Pack creation for clientRequestId: {}", request.getCustomerId());
	        return PackConverter.toResponseDTO(existingPack.get());
	    }

	    try {
	    	Customer customer = customerRepository.findById(request.getCustomerId());
	    	
	    	Boolean isHoliday = apiNagerService.isHoliday(request.getEstimatedDeliveryDate());
			request.setIsHolliday(isHoliday);
			request.setCustomerId(customer.getId());

			String funFact = apiTheDogService.getFunFact();
			
			Pack pack = PackConverter.toEntity(request);
			pack.setCustomer(customer);
			pack.setFunFact(funFact);
					
			Pack savedPack = packRepository.save(pack);

			PackResponseDTO response = PackConverter.toResponseDTO(savedPack);
			
			logger.info("[FINISH] - createPack() id: {}", savedPack.getId());
			return response;
	    } catch (CustomerNotFoundException ex) {
	    	logger.error("[FINISH] - createPack() WITH ERRORS ex: {}", ex.getMessage());
	    	throw new CustomerNotFoundException("");
	    }
	}

	@Override
	@CachePut(value = "packsById", key = "#id")
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
		
		logger.info("[FINISH] - updateStatusPack() id: {}", updatedPack.getId());
		return response;
	}

	@Override
	@Cacheable(value = "packsById", key = "#id")
	public Pack getPackById(Long id) {
		logger.info("[START] - getPackById() id: {}", id);
		
		Pack response = packRepository.findById(id)
				.orElseThrow(() -> new PackNotFoundException("Pack not found with id: " + id));

		logger.info("[FINISH] - getPackById() id: {}", id);
		return response;
	}

	@Override
	@CacheEvict(value = "packsById", key = "#id")
	public PackCancelResponseDTO cancelPack(Long id) throws BadRequestException {
		logger.info("[START] - cancelPack() id: {}", id);

		Pack pack = getPackById(id);
		
		PackValidation.validatePackElegibleForCancellation(pack.getStatus());
		
		pack.setStatus(PackageStatus.CANCELLED);
		PackCancelResponseDTO response = PackConverter.toCancelResponseDTO(packRepository.save(pack));
		
		logger.info("[FINISH] - cancelPack() id: {}", id);
		return response;
	}
		
	@Override
	@Cacheable(value = "packs", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
	public Page<PackResponseDTO> getPacks(String sender, String recipient, Pageable pageable) {
		logger.info("[START] - getPacks() pageable: {}", pageable);
		Page<PackResponseDTO> response = packHelperService.getPackEvents(sender, recipient, pageable);
		
		logger.info("[FINISH] - getPacks() total elements: {}, total pages: {}", response.getTotalElements(), response.getTotalPages());
		return response;
	}

	@Override
	@Cacheable(value = "packsIncludeEvents", key = "#id + '-' + #includeEvents + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
	public PackResponseDTO getPackByIdAndIncludeEvents(Long id, Boolean includeEvents, Pageable pageable) {
		logger.info("[START] - getPackByIdAndIncludeEvents() id: {}, includeEvents: {}", id, includeEvents);
		
		Pack pack = getPackById(id);
		Page<PackEvent> events = includeEvents ? packEventHelperService.findByPackId(id, pageable) : Page.empty();
		PackResponseDTO response = PackConverter.listEventToResponseDTO(pack, events);

		logger.info("[FINISH] - getPackByIdAndIncludeEvents() id: {}", id);
		return response;
	}

	@Override
    public CacheControl getCacheControl() {
        return CacheControl.maxAge(5, TimeUnit.MINUTES);
    }
}
