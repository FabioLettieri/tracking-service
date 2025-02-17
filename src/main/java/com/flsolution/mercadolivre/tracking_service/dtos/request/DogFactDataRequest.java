package com.flsolution.mercadolivre.tracking_service.dtos.request;

import com.flsolution.mercadolivre.tracking_service.dtos.response.DogFactAttributesResponse;

public record DogFactDataRequest (
		String id,
		String type,
		DogFactAttributesResponse attributes
		) {
}
