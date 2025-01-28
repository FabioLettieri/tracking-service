package com.flsolution.mercadolivre.tracking_service.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PackRequestDTO {

	@NotNull(message = "description is mandatory")
	private String description;
	
	@NotNull(message = "sender is mandatory")
	private String sender;
	
	@NotNull(message = "recipient is mandatory")
	private String recipient;
	
	@NotNull(message = "IsHolliday is mandatory")
	private Boolean isHolliday;
	
	private String funfact;
	private String estimatedDeliveryDate;
	
}
