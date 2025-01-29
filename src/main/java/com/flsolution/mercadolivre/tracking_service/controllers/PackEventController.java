package com.flsolution.mercadolivre.tracking_service.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flsolution.mercadolivre.tracking_service.dtos.PackEventDTO;
import com.flsolution.mercadolivre.tracking_service.services.PackEventRequestDTO;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackEventServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pack-events")
public class PackEventController {
	private static final Logger logger = LoggerFactory.getLogger(PackEventController.class);
	private final PackEventServiceImpl packEventServiceImpl;
	
	@GetMapping
	public ResponseEntity<List<PackEventDTO>> getPacks(
	        @RequestParam(required = false) String sender,
	        @RequestParam(required = false) String recipient) {

	    logger.info("[START] - getPacks() sender: {}, recipient: {}", sender, recipient);

	    List<PackEventDTO> response = packEventServiceImpl.getPackEvents(sender, recipient);

	    logger.info("[FINISH] - getPacks()");
	    return ResponseEntity.ok(response);
	}
	
	@PostMapping
    public ResponseEntity<PackEventDTO> createPackEvent(@RequestBody @Valid PackEventRequestDTO requestDTO) {
        logger.info("[START] - createPackEvent()");

        PackEventDTO response = packEventServiceImpl.createPackEvent(requestDTO);

        logger.info("[FINISH] - createPackEvent()");
        return ResponseEntity.ok(response);
    }
	
	
}
