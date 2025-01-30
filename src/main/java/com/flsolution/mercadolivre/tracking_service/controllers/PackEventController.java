package com.flsolution.mercadolivre.tracking_service.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.services.ETagService;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventProducerServiceImpl;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pack-events")
public class PackEventController {
	private static final Logger logger = LoggerFactory.getLogger(PackEventController.class);
	
	private final PackEventServiceImpl packEventServiceImpl;
	private final PackEventProducerServiceImpl packEventProducerServiceImpl;
	private final ETagService eTagService;
	
	@Operation(summary = "Buscar pacote por todos os eventos", description = "Recupera as informações de todos os eventos registrados paginado")
	@ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso.")
	@GetMapping
	public ResponseEntity<Page<PackEventDTO>> getPackEvents(
			@PageableDefault(size = 50) Pageable pageable,
			HttpServletRequest request) {

	    logger.info("[START] - getPackEvents() pageable: {}", pageable);

	    Page<PackEventDTO> response = packEventServiceImpl.getPackEvents(pageable);
	    
	    String eTag = eTagService.generateETag(response);
        
        if (eTagService.isNotModified(request, eTag)) {
        	logger.info("[FINISH] - getPackEvents() CACHE NOT_MODIFIED");
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        
        logger.info("[FINISH] - getPackEvents()");
        return ResponseEntity.ok()
                .header(HttpHeaders.ETAG, eTag)
                .cacheControl(packEventServiceImpl.getCacheControl())
                .body(response);
	}
	
	@Operation(summary = "Criar um novo evento do pacote", description = "Cria um novo evento com status de atualizações")
	@ApiResponse(responseCode = "200", description = "PackEvent criado com sucesso.", content = @Content(schema = @Schema(implementation = PackEventRequestDTO.class)))
	@ApiResponse(responseCode = "400", description = "PackEvent não foi criado por falta de parametros e/ou por parametros errados.")
	@ApiResponse(responseCode = "404", description = "PackEvent não foi criado por ID informado errado.")
	@PostMapping
    public ResponseEntity<String> createPackEvent(@RequestBody @Valid PackEventRequestDTO requestDTO) throws Exception {
        logger.info("[START] - createPackEvent()");

        packEventProducerServiceImpl.sendPackEvent(requestDTO);

        logger.info("[FINISH] - createPackEvent()");
        return ResponseEntity.status(HttpStatus.CREATED).body("O Evento foi enviado para o processamento.");
    }

	@Operation(summary = "Criar um novo evento do pacote", description = "Cria um novo evento com status de atualizações")
	@ApiResponse(responseCode = "200", description = "PackEvent criado com sucesso.", content = @Content(schema = @Schema(implementation = PackEventRequestDTO.class)))
	@ApiResponse(responseCode = "400", description = "PackEvent não foi criado por falta de parametros e/ou por parametros errados.")
	@ApiResponse(responseCode = "404", description = "PackEvent não foi criado por ID informado errado.")
	@PostMapping("/list")
	public ResponseEntity<String> createListPackEvent(@RequestBody @Valid List<PackEventRequestDTO> requestDTO) throws Exception {
		logger.info("[START] - createPackEvent()");
		
		packEventProducerServiceImpl.sendListPackEvent(requestDTO);
		
		logger.info("[FINISH] - createPackEvent()");
		return ResponseEntity.status(HttpStatus.CREATED).body("O Evento foi enviado para o processamento.");
	}
	
	
}
