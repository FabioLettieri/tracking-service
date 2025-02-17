package com.flsolution.mercadolivre.tracking_service.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponse(
		Long id,
		String name,
	    String document,
	    String phoneNumber,
	    String email,
	    String address,
	    List<Pack> packs
		) {

}
