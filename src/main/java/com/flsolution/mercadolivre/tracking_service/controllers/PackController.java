package com.flsolution.mercadolivre.tracking_service.controllers;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.flsolution.mercadolivre.tracking_service.services.impl.PackServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/packs")
public class PackController {
	private static final Logger logger = LoggerFactory.getLogger(PackController.class);
	
	private final PackServiceImpl packServiceImpl;
	
	@PostMapping
	public ResponseEntity<PackResponseDTO> createPack(@RequestBody @Valid PackRequestDTO request) throws Exception {
		logger.info("[START] - createPack() request: {}", request);
		
		PackResponseDTO response = packServiceImpl.createPack(request);
		
		logger.info("[FINISH] - createPack()");
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}/status")
	public ResponseEntity<PackResponseDTO> updatePackStatus(@PathVariable Long id, @RequestBody @Valid UpdateStatusRequest request) {
		logger.info("[START] - updatePackStatus() request: {}", request);
		
		PackResponseDTO response = packServiceImpl.updateStatusPack(id, request.getStatus());
		
		logger.info("[FINISH] - updatePackStatus()");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PackResponseDTO> getPackById(
		    @PathVariable Long id,
		    @RequestParam(required = false, defaultValue = "false") boolean includeEvents,
		    @PageableDefault(size = 50) Pageable pageable) {

        logger.info("[START] - getPackById() id: {}, includeEvents: {}", id, includeEvents);

        PackResponseDTO response = packServiceImpl.getPackByIdAndIncludeEvents(id, includeEvents, pageable);

        logger.info("[FINISH] - getPackById()");
        return ResponseEntity.ok(response);
    }
	
	@GetMapping
	public ResponseEntity<Page<PackResponseDTO>> getPacks(
			@RequestParam(required = false) String sender,
	        @RequestParam(required = false) String recipient,
		    @PageableDefault(size = 50) Pageable pageable) {

		logger.info("[START] - getPacks() sender: {}, recipient: {}, pageable: {}", sender, recipient, pageable);
		
		Page<PackResponseDTO> response = packServiceImpl.getPacks(sender, recipient, pageable);
		
		logger.info("[FINISH] - getPacks()");
        return ResponseEntity.ok(response);
    }
	
	@PutMapping("/{id}/cancel")
	public ResponseEntity<PackCancelResponseDTO> cancelPack(@PathVariable Long id) throws BadRequestException {
		logger.info("[START] - cancelPack() id: {}", id);
		
		PackCancelResponseDTO response = packServiceImpl.cancelPack(id);
		
		logger.info("[FINISH] - cancelPack()");
		return ResponseEntity.ok(response);
	}
	
}
