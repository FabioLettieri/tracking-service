package com.flsolution.mercadolivre.tracking_service.dtos;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackEventRequestDTO {

	@NotNull(message = "O ID do pacote é obrigatório")
    private Long packId;

	@NotNull(message = "A localização do evento é obrigatória")
    private String location;

	@NotNull(message = "A descrição do evento é obrigatória")
    private String description;
	
	private LocalDateTime eventDateTime = LocalDateTime.now();
	
	public Instant getEventDateTimeAsInstant() {
        return eventDateTime != null ? eventDateTime.toInstant(ZoneOffset.UTC) : Instant.now();
    }

}
