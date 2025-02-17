package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flsolution.mercadolivre.tracking_service.dtos.request.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.CustomerResponse;

public interface CustomerServiceImpl {
	
	CustomerResponse createCustomer(CustomerRequest customerDTO);

	Page<CustomerResponse> getCustomers(String sender, String recipient, Pageable pageable);

}
