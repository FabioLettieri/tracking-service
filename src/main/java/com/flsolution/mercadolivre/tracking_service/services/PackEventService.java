package com.flsolution.mercadolivre.tracking_service.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	public List<PackEvent> findByPackId(Long id) {
		logger.info("[START] - findPackById() id: {}", id);
		
		List<PackEvent> eventPacks = packEventHelperService.findByPackId(id);

		logger.info("[FINISH] - findPackById()");
		return eventPacks;
	}
	
	@Override
    public List<PackEventDTO> getPackEvents(String sender, String recipient) {
        logger.info("[START] - getPackEvents() sender: {}, recipient: {}", sender, recipient);

        List<PackEventDTO> response = packEventHelperService.getPackEvents(sender, recipient);

        logger.info("[FINISH] - getPackEvents() total found: {}", response.size());
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
