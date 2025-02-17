package com.flsolution.mercadolivre.tracking_service.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.dtos.response.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Customer;

public class CustomerConverter {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerConverter.class);
	
	public static CustomerResponse toResponse(Customer customer) {
		logger.info("[START] - toResponse() customer: {}", customer);
		
		var response = new CustomerResponse(
				customer.getId(),
				customer.getName(),
				customer.getDocument(),
				customer.getPhoneNumber(),
				customer.getEmail(),
				customer.getAddress(),
				customer.getPacks()
				);
		
		logger.info("[FINISH] - toResponse()");
		return response;
	}

}
