package com.flsolution.mercadolivre.tracking_service.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record PackEventDTO (
		Long id,
		Long packId,
		String location,
		String description,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
		LocalDateTime eventDateTime
		) {
	
}
