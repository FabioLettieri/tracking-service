package com.flsolution.mercadolivre.tracking_service.converters;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

public class PackConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(PackConverter.class);

	public static Pack toEntity(PackRequestDTO request) {
		logger.info("[START] - toEntity() request: {}", request);
		Pack response = Pack.builder()
				.description(request.getDescription())
				.sender(request.getSender())
				.recipient(request.getRecipient())
				.isHolliday(request.getIsHolliday())
				.estimatedDeliveryDate(request.getEstimatedDeliveryDate())
				.status(PackageStatus.CREATED)
				.build();
		
		logger.info("[FINISH] - toEntity()");
		return response;
	}
	
	public static PackResponseDTO toResponseDTO(Pack pack) {
		logger.info("[START] - toResponseDTO() pack: {}", pack);
		PackResponseDTO response = new PackResponseDTO(
				pack.getId(),
				pack.getDescription(),
				pack.getSender(),
				pack.getRecipient(),
				pack.getStatus(),
				pack.getCreatedAt(),
				pack.getUpdatedAt());
		
		if (pack.getDeliveredAt() != null) {
	        response.setDeliveredAt(pack.getDeliveredAt());
	    }
		
		
		logger.info("[FINISH] - toResponseDTO()");
		return response;
	}
	
	public static PackResponseDTO listEventToResponseDTO(Pack pack, Page<PackEvent> events) {
		logger.info("[START] - listEventToResponseDTO() pack: {}, events: {}", pack, events);
        List<PackEventDTO> eventDTOs = events.stream()
                .map(event -> new PackEventDTO(
                		event.getId(),
                        pack.getId(),
                        event.getLocation(),
                        event.getDescription(),
                        event.getEventDateTime()
                ))
                .collect(Collectors.toList());
        
        PackResponseDTO response = PackResponseDTO.builder()
                .id(pack.getId())
                .description(pack.getDescription())
                .sender(pack.getSender())
                .recipient(pack.getRecipient())
                .status(pack.getStatus())
                .createdAt(pack.getCreatedAt())
                .updatedAt(pack.getUpdatedAt())
                .deliveredAt(pack.getDeliveredAt())
                .events(eventDTOs.isEmpty() ? null : eventDTOs)
                .build();
        
        logger.info("[FINISH] - listEventToResponseDTO()");
        return response;
    }
	
	public static PackCancelResponseDTO toCancelResponseDTO(Pack pack) {
		logger.info("[START] - toCancelResponseDTO() pack: {}", pack);
		PackCancelResponseDTO response = PackCancelResponseDTO.builder()
				.id(pack.getId())
				.status(pack.getStatus())
				.updatedAt(pack.getUpdatedAt())
				.build();
		
		logger.info("[FINISH] - toCancelResponseDTO()");
		return response;
	}

	public static Page<PackResponseDTO> toListPackResponseDTO(Page<Pack> packs) {
		Page<PackResponseDTO> responseDTOs = packs.map(event -> new PackResponseDTO(
                		event.getId(),
                        event.getDescription(),
                        event.getSender(),
                        event.getRecipient(),
                        event.getStatus(),
                        event.getCreatedAt(),
                        event.getUpdatedAt()
                ));
		
		return responseDTOs;
	}

}
