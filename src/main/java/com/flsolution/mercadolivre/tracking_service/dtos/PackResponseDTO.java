package com.flsolution.mercadolivre.tracking_service.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
	
}
