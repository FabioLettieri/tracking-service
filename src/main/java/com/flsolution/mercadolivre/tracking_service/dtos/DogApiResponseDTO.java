package com.flsolution.mercadolivre.tracking_service.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DogApiResponseDTO {
    private List<DogFactDataDTO> data;
}
