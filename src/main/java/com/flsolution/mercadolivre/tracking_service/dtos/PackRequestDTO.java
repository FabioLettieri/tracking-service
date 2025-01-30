package com.flsolution.mercadolivre.tracking_service.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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

	@Schema(description = "Descrição do pacote", example = "Pacote frágil - vidro")
    @NotNull(message = "description is mandatory")
    private String description;

    @Schema(description = "Nome do remetente", example = "João da Silva")
    @NotNull(message = "sender is mandatory")
    private String sender;

    @Schema(description = "Nome do destinatário", example = "Maria Oliveira")
    @NotNull(message = "recipient is mandatory")
    private String recipient;

    @Schema(description = "Indica se a entrega será em um feriado", example = "true")
    @NotNull(message = "IsHolliday is mandatory")
    private Boolean isHolliday;

    @Schema(description = "Data estimada de entrega no formato yyyy-MM-dd", example = "2025-01-01")
    @NotNull(message = "estimatedDeliveryDate is mandatory")
    private String estimatedDeliveryDate;
	
}
