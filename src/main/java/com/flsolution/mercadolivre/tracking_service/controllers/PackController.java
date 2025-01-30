package com.flsolution.mercadolivre.tracking_service.controllers;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flsolution.mercadolivre.tracking_service.dtos.PackCancelResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.updates.UpdateStatusRequest;
import com.flsolution.mercadolivre.tracking_service.services.ETagService;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/packs")
@Tag(name = "Packs", description = "Gerenciamento de pacotes")
public class PackController {
	private static final Logger logger = LoggerFactory.getLogger(PackController.class);
	
	private final PackServiceImpl packServiceImpl;
	private final ETagService eTagService;
	
	@Operation(summary = "Criar um novo pacote", description = "Cria um novo pacote para rastreamento")
	@ApiResponse(responseCode = "200", description = "Pacote criado com sucesso.", content = @Content(schema = @Schema(implementation = PackResponseDTO.class)))
	@ApiResponse(responseCode = "400", description = "Pacote não foi criado por falta de parametros e/ou por parametros errados.")
	@PostMapping
	public ResponseEntity<PackResponseDTO> createPack(@RequestBody @Valid PackRequestDTO request) {
		logger.info("[START] - createPack() request: {}", request);
		
		PackResponseDTO response = packServiceImpl.createPack(request);
		
		logger.info("[FINISH] - createPack()");
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Atualizar status do pacote", description = "Atualiza o status de um pacote específico")
	@ApiResponse(responseCode = "200", description = "Status atualizado com sucesso.", content = @Content(schema = @Schema(implementation = PackResponseDTO.class)))
	@ApiResponse(responseCode = "400", description = "Status inválido.")
	@PutMapping("/{id}/status")
	public ResponseEntity<PackResponseDTO> updatePackStatus(@PathVariable Long id, @RequestBody @Valid UpdateStatusRequest request) {
		logger.info("[START] - updatePackStatus() request: {}", request);
		
		PackResponseDTO response = packServiceImpl.updateStatusPack(id, request.getStatus());
		
		logger.info("[FINISH] - updatePackStatus()");
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Buscar pacote por um ID", description = "Recupera as informações de um pacote pelo seu ID")
	@ApiResponse(responseCode = "200", description = "Pacote encontrado", content = @Content(schema = @Schema(implementation = PackResponseDTO.class)))
	@ApiResponse(responseCode = "404", description = "Pacote não encontrado")
	@GetMapping("/{id}")
	public ResponseEntity<PackResponseDTO> getPackById(
		    @PathVariable Long id,
		    @RequestParam(required = false, defaultValue = "false") boolean includeEvents,
		    @PageableDefault(size = 50) Pageable pageable,
	        HttpServletRequest request) {

        logger.info("[START] - getPackById() id: {}, includeEvents: {}", id, includeEvents);

        PackResponseDTO response = packServiceImpl.getPackByIdAndIncludeEvents(id, includeEvents, pageable);
        
        String eTag = eTagService.generateETag(response);
        
        if (eTagService.isNotModified(request, eTag)) {
        	logger.info("[FINISH] - getPackById() CACHE NOT_MODIFIED");
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        logger.info("[FINISH] - getPackById()");
        return ResponseEntity.ok()
                .header(HttpHeaders.ETAG, eTag)
                .cacheControl(packServiceImpl.getCacheControl())
                .body(response);
    }
	
	@Operation(summary = "Buscar todos os pacotes", description = "Retorna todos os pacotes paginado")
	@ApiResponse(responseCode = "200", description = "Pacotes encontrados", content = @Content(schema = @Schema(implementation = PackResponseDTO.class)))
	@GetMapping
	public ResponseEntity<Page<PackResponseDTO>> getPacks(
			@RequestParam(required = false) String sender,
	        @RequestParam(required = false) String recipient,
		    @PageableDefault(size = 50) Pageable pageable,
	        HttpServletRequest request) {

		logger.info("[START] - getPacks() sender: {}, recipient: {}, pageable: {}", sender, recipient, pageable);
		
		Page<PackResponseDTO> response = packServiceImpl.getPacks(sender, recipient, pageable);

		String eTag = eTagService.generateETag(response);
		
		if (eTagService.isNotModified(request, eTag)) {
			logger.info("[FINISH] - getPacks() CACHE NOT_MODIFIED");
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
		}
		
		logger.info("[FINISH] - getPacks()");
		return ResponseEntity.ok()
				.header(HttpHeaders.ETAG, eTag)
				.cacheControl(packServiceImpl.getCacheControl())
				.body(response);
    }

	@Operation(summary = "Cancelar um pacote específico", description = "Cancela um pacote antes da entrega ao cliente")
	@ApiResponse(responseCode = "200", description = "Cancelamento efetuado", content = @Content(schema = @Schema(implementation = PackCancelResponseDTO.class)))
	@ApiResponse(responseCode = "400", description = "Cancelamento não realizado")
	@PutMapping("/{id}/cancel")
	public ResponseEntity<PackCancelResponseDTO> cancelPack(@PathVariable Long id) throws BadRequestException {
		logger.info("[START] - cancelPack() id: {}", id);
		
		PackCancelResponseDTO response = packServiceImpl.cancelPack(id);
		
		logger.info("[FINISH] - cancelPack()");
		return ResponseEntity.ok(response);
	}
}
