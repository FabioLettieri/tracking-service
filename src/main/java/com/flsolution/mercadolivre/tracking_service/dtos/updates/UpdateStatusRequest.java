package com.flsolution.mercadolivre.tracking_service.dtos.updates;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {

	@Schema(description = "", example = "IN_TRANSIT, DELIVERED, CANCELLED, ")
	@NotNull(message = "Status is mandatory.")
	private PackageStatus status;
	
}
