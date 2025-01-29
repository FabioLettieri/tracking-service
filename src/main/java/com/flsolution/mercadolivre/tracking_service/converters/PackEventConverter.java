package com.flsolution.mercadolivre.tracking_service.converters;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;

public class PackEventConverter {
	
	public static PackEventDTO toDTO(PackEvent event) {
        return new PackEventDTO(
                event.getPack().getId(),
                event.getLocation(),
                event.getDescription(),
                event.getEventDateTime().atOffset(ZoneOffset.UTC).toLocalDateTime()
        );
    }
	
	public static PackEvent toEntity(PackEventRequestDTO requestDTO, Pack pack) {
		return PackEvent.builder()
				.pack(pack)
				.location(requestDTO.getLocation())
				.description(requestDTO.getDescription())
				.eventDateTime(LocalDateTime.now())
				.build();
	}
	
	

}
