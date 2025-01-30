package com.flsolution.mercadolivre.tracking_service.dtos;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PackEventRequestDTO {

	@Schema(description = "Id de relação entre o evento e o Pacote", example = "1")
	@NotNull(message = "O ID do pacote é obrigatório")
    private Long packId;

	@Schema(description = "Localizacao atual", example = "Despachado, Iniciou a rota, Com a transportadora, etc.")
	@NotNull(message = "A localização do evento é obrigatória")
    private String location;

	@Schema(description = "momento atual do pedido", example = "Pacote chegou ao Centro de Distribuição, Aguardando coleta, etc.")
	@NotNull(message = "A descrição do evento é obrigatória")
    private String description;
	
	private LocalDateTime eventDateTime = LocalDateTime.now();
	
	public Instant getEventDateTimeAsInstant() {
        return eventDateTime != null ? eventDateTime.toInstant(ZoneOffset.UTC) : Instant.now();
    }

}
