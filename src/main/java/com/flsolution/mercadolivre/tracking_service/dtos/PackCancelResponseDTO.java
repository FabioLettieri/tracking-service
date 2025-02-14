package com.flsolution.mercadolivre.tracking_service.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import lombok.Builder;

@Builder
public record PackCancelResponseDTO (
		Long id,
		PackageStatus status,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
		LocalDateTime updatedAt
		) {
	
}
