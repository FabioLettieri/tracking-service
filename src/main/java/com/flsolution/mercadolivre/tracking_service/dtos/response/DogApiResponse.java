package com.flsolution.mercadolivre.tracking_service.dtos.response;

import java.util.List;

import com.flsolution.mercadolivre.tracking_service.dtos.request.DogFactDataRequest;

public record DogApiResponse (List<DogFactDataRequest> data) {

}
