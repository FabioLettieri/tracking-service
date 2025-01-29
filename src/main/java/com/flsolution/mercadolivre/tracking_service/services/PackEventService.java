package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.PackEventConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.repositories.PackEventRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackEventService implements PackEventServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(PackEventService.class);
	
	private final PackEventHelperService packEventHelperService;
	
	private final PackEventRepository packEventRepository;
	private final PackService packService;
	
	@Override
	public Page<PackEvent> findByPackId(Long id, Pageable pageable) {
		logger.info("[START] - findPackById() id: {}", id);
		
		Page<PackEvent> eventPacks = packEventHelperService.findByPackId(id, pageable);

		logger.info("[FINISH] - findPackById()");
		return eventPacks;
	}
	
	@Override
    public Page<PackEventDTO> getPackEvents(Pageable pageable) {
        logger.info("[START] - getPackEvents() sender: {}, pageable: {}", pageable);

        Page<PackEventDTO> response = packEventHelperService.getPackEvents(pageable);

        logger.info("[FINISH] - getPackEvents() total found: {}, total pages: {}", response.getTotalElements(), response.getTotalPages());
        return response;
    }

	@Override
	@Transactional
	public PackEventDTO createPackEvent(PackEventRequestDTO requestDTO) {
		logger.info("[START] - createPackEvent() requestDTO: {}", requestDTO);
		
		Pack pack = packService.getPackById(requestDTO.getPackId());
		PackEvent packEvent = PackEventConverter.toEntity(requestDTO, pack);
		
		PackEvent savedPackEvent = packEventRepository.save(packEvent);
		
		PackEventDTO response = PackEventConverter.toDTO(savedPackEvent);
		
		logger.info("[FINISH] - createPackEvent()");
		return response;
	}


}
