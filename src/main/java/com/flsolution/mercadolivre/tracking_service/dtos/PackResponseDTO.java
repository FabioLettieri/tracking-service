package com.flsolution.mercadolivre.tracking_service.dtos;

import java.time.LocalDateTime;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PackResponseDTO {
	
	private Long id;
	private String description;
	private String sender;
	private String recipient;
	private PackageStatus status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
