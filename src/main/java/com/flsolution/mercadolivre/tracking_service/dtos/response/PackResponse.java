package com.flsolution.mercadolivre.tracking_service.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PackResponse (
	Long id,
	String description,
	String sender,
	String recipient,
	PackageStatus status,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	LocalDateTime deliveredAt,
	List<PackEventResponse> events
		) {
}
