package com.flsolution.mercadolivre.tracking_service.dtos;

import java.util.List;

import com.flsolution.mercadolivre.tracking_service.entities.Pack;

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
