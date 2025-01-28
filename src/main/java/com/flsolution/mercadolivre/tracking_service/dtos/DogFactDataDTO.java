package com.flsolution.mercadolivre.tracking_service.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DogFactDataDTO {
	private String id;
    private String type;
    private DogFactAttributesDTO attributes;
}
