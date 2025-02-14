package com.flsolution.mercadolivre.tracking_service.services.impl;

import com.flsolution.mercadolivre.tracking_service.dtos.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.CustomerResponse;

public interface CustomerServiceImpl {
	
	CustomerResponse createCustomer(CustomerRequest customerDTO);

}
