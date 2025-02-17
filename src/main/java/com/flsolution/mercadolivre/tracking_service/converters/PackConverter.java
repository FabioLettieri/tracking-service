package com.flsolution.mercadolivre.tracking_service.converters;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.flsolution.mercadolivre.tracking_service.dtos.request.PackRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackCancelResponse;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

public class PackConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(PackConverter.class);

	public static Pack toEntity(PackRequest request) {
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
	
	public static PackResponse toResponseDTO(Pack pack) {
		logger.info("[START] - toResponseDTO() pack: {}", pack);
		PackResponse response = PackResponse.builder()
				.createdAt(pack.getCreatedAt())
				.deliveredAt(pack.getDeliveredAt() != null ? pack.getDeliveredAt() : null)
				.description(pack.getDescription())
				.events(pack.getEvents() != null ? pack.getEvents().stream().map(PackEventConverter::toDTO).collect(Collectors.toList()) : new ArrayList<PackEventResponse>())
				.id(pack.getId())
				.recipient(pack.getRecipient())
				.sender(pack.getSender())
				.status(pack.getStatus())
				.updatedAt(pack.getUpdatedAt())
				.build();
		
		logger.info("[FINISH] - toResponseDTO()");
		return response;
	}
	
	public static PackResponse listEventToResponseDTO(Pack pack, Page<PackEvent> events) {
		logger.info("[START] - listEventToResponseDTO() pack: {}, events: {}", pack, events);
        List<PackEventResponse> eventDTOs = events.stream()
                .map(event -> new PackEventResponse(
                		event.getId(),
                        pack.getId(),
                        event.getLocation(),
                        event.getDescription(),
                        event.getEventDateTime()
                ))
                .collect(Collectors.toList());
        
        PackResponse response = PackResponse.builder()
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
	
	public static PackCancelResponse toCancelResponseDTO(Pack pack) {
		logger.info("[START] - toCancelResponseDTO() pack: {}", pack);
		PackCancelResponse response = PackCancelResponse.builder()
				.id(pack.getId())
				.status(pack.getStatus())
				.updatedAt(pack.getUpdatedAt())
				.build();
		
		logger.info("[FINISH] - toCancelResponseDTO()");
		return response;
	}

	public static Page<PackResponse> toListPackResponseDTO(Page<Pack> packs) {
		logger.info("[START] - toListPackResponseDTO() packs: {}", packs);
		Page<PackResponse> responseDTOs = packs.map(event -> toResponseDTO(event));
		
		logger.info("[FINISH] - toListPackResponseDTO()");
		return responseDTOs;
	}

}
