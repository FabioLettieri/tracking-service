package com.flsolution.mercadolivre.tracking_service.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flsolution.mercadolivre.tracking_service.dtos.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.services.ETagService;
import com.flsolution.mercadolivre.tracking_service.services.impl.CustomerServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Gerenciamento de usuários")
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	private final CustomerServiceImpl customerServiceImpl;
	private final ETagService etagService;
	
	@Operation(summary = "Criar um novo cliente", description = "Cria um novo cliente para inserção de pacotes")
	@ApiResponse(responseCode = "200", description = "Cliente criado com sucesso.", content = @Content(schema = @Schema(implementation = CustomerResponse.class)))
	@ApiResponse(responseCode = "400", description = "Cliente não foi criado por falta de parametros e/ou por parametros errados.")
	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@RequestBody @Valid CustomerRequest request) {
		logger.info("[START] - createCustomer() request: {}", request);
		CustomerResponse response = customerServiceImpl.createCustomer(request);
		
		logger.info("[FINISH] - createCustomer()");
		return ResponseEntity.ok(response);
	}
	
}
