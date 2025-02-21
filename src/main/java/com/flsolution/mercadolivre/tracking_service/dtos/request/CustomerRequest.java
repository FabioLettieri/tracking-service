package com.flsolution.mercadolivre.tracking_service.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CustomerRequest(
		@Schema(description = "Nome do usuário", example = "Pedro Silva Custódio")
		@NotNull(message = "O Nome do usuário é obrigatório")
	    String name,
	    
	    @Schema(description = "CPF do usuário", example = "123.456.789-00")
		@NotNull(message = "O CPF do usuário é obrigatório")
	    String document,
	    
	    @Schema(description = "Número do celular do usuário", example = "11987654321")
		@NotNull(message = "O Número celular do usuário é obrigatório")
	    String phoneNumber,
	    
	    @Schema(description = "E-mail do usuário", example = "pedrosilvacustodio@email.com")
		@NotNull(message = "O E-maill do usuário é obrigatório")
	    String email,
	    
	    @Schema(description = "Endereço residencial do usuário", example = "Rua dos pinheiros, 123 - Jardim Nosso Lar")
		@NotNull(message = "O Endereço do usuário é obrigatório")
	    String address
	    
) {
    
}
