package com.flsolution.mercadolivre.tracking_service.controllers;

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
	
	@PostMapping
    public ResponseEntity<String> createPackEvent(@RequestBody @Valid PackEventRequestDTO requestDTO) throws Exception {
        logger.info("[START] - createPackEvent()");

        packEventProducerServiceImpl.sendPackEvent(requestDTO);

        logger.info("[FINISH] - createPackEvent()");
        return ResponseEntity.status(HttpStatus.CREATED).body("O Evento foi enviado para o processamento.");
    }
	
	
}
