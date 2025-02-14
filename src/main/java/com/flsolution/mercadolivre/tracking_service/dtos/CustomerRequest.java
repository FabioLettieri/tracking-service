package com.flsolution.mercadolivre.tracking_service.dtos;

import lombok.Builder;

@Builder
public record CustomerRequest(
	    String name,
	    String document,
	    String phoneNumber,
	    String email,
	    String address
	    
) {
    
}
