package com.flsolution.mercadolivre.tracking_service.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flsolution.mercadolivre.tracking_service.dtos.PackRequestDTO;
import com.flsolution.mercadolivre.tracking_service.dtos.PackResponseDTO;
import com.flsolution.mercadolivre.tracking_service.services.impl.PackServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/packs")
public class PackController {
	private static final Logger logger = LoggerFactory.getLogger(PackController.class);
	private final PackServiceImpl packService;
	
	@PostMapping
	public ResponseEntity<PackResponseDTO> createPack(@RequestBody PackRequestDTO request) {
		logger.info("[START] - createPack() request: {}", request);
		
		PackResponseDTO response = packService.createPack(request);
		
		logger.info("[FINISH] - createPack()");
		
		return ResponseEntity.ok(response);
	}
	
}
