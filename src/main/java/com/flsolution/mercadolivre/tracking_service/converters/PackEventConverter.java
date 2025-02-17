package com.flsolution.mercadolivre.tracking_service.converters;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.flsolution.mercadolivre.tracking_service.dtos.request.PackEventRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.PackEventResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;

public class PackEventConverter {
	
	public static PackEventResponse toDTO(PackEvent event) {
        return new PackEventResponse(
        		event.getId(),
                event.getPack().getId(),
                event.getLocation(),
                event.getDescription(),
                event.getEventDateTime().atOffset(ZoneOffset.UTC).toLocalDateTime()
        );
    }
	
	public static PackEvent toEntity(PackEventRequest requestDTO, Pack pack) {
		return PackEvent.builder()
				.pack(pack)
				.location(requestDTO.location())
				.description(requestDTO.description())
				.eventDateTime(LocalDateTime.now())
				.build();
	}
	
	

}
