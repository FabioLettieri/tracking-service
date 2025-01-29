package com.flsolution.mercadolivre.tracking_service.dtos.updates;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {

	@NotNull(message = "Status is mandatory.")
	private PackageStatus status;
	
}
