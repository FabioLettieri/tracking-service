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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flsolution.mercadolivre.tracking_service.dtos.request.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.services.ETagService;
import com.flsolution.mercadolivre.tracking_service.services.impl.CustomerServiceImpl;
import com.flsolution.mercadolivre.tracking_service.utils.CacheControlUtils;

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
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Gerenciamento de usuários")
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	private final CustomerServiceImpl customerServiceImpl;
	private final ETagService eTagService;
	
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
	
	@Operation(summary = "Buscar todos os customers", description = "Retorna todos os customers paginado")
	@ApiResponse(responseCode = "200", description = "Customers encontrados", content = @Content(schema = @Schema(implementation = CustomerResponse.class)))
	@GetMapping
	public ResponseEntity<Page<CustomerResponse>> getCustomers(
			@RequestParam(required = false) String sender,
	        @RequestParam(required = false) String recipient,
		    @PageableDefault(size = 50) Pageable pageable,
	        HttpServletRequest request) {

		logger.info("[START] - getCustomers() sender: {}, recipient: {}, pageable: {}", sender, recipient, pageable);
		
		Page<CustomerResponse> response = customerServiceImpl.getCustomers(sender, recipient, pageable);

		String eTag = eTagService.generateETag(response);
		
		if (eTagService.isNotModified(request, eTag)) {
			logger.info("[FINISH] - getCustomers() CACHE NOT_MODIFIED");
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
		}
		
		logger.info("[FINISH] - getCustomers()");
		return ResponseEntity.ok()
				.header(HttpHeaders.ETAG, eTag)
				.cacheControl(CacheControlUtils.getCacheControl())
				.body(response);
    }
	
}
