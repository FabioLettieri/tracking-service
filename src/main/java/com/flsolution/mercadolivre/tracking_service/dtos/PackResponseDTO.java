package com.flsolution.mercadolivre.tracking_service.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackResponseDTO {
	
	public PackResponseDTO(
			Long id, 
			String description, 
			String sender, 
			String recipient, 
			PackageStatus status, 
			LocalDateTime createdAt, 
			LocalDateTime updatedAt) {
		
		this.id = id;
		this.description  = description;
		this.sender = sender;
		this.recipient = recipient;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		
	}
	
	private Long id;
	private String description;
	private String sender;
	private String recipient;
	private PackageStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deliveredAt;
	private List<PackEventDTO> events;
	
}
