package com.flsolution.mercadolivre.tracking_service.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.dtos.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Customer;

public class CustomerConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerConverter.class);
	
	public static CustomerResponse toResponseDTO(Customer customer) {
		logger.info("[START] - toResponseDTO() customer: {}", customer);
		
		var response = new CustomerResponse(
				customer.getId(),
				customer.getName(),
				customer.getDocument(),
				customer.getPhoneNumber(),
				customer.getEmail(),
				customer.getAddress(),
				customer.getPacks()
				);
		
		logger.info("[FINISH] - toResponseDTO()");
		return response;
	}

}
