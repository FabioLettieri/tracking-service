package com.flsolution.mercadolivre.tracking_service.services;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.converters.CustomerConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Customer;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerNotFoundException;
import com.flsolution.mercadolivre.tracking_service.repositories.CustomerRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.CustomerServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerService implements CustomerServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	private final CustomerRepository customerRepository;
	
	@Override
	public CustomerResponse createCustomer(CustomerRequest request) {
		logger.info("[START] - createCustomer() request: {}", request);
		
		var customer = Customer.builder()
				.address(request.address())
				.document(request.document())
				.email(request.email())
				.name(request.name())
				.phoneNumber(request.phoneNumber())
				.packs(new ArrayList<Pack>())
				.build();

        customer = customerRepository.save(customer);
        var response = CustomerConverter.toResponseDTO(customer);
        
        logger.info("[FINISH] - createCustomer()");
        return response;
	}

	public Customer findById(Long customerId) throws CustomerNotFoundException {
		logger.info("[START] - findById() customerId: {}", customerId);

		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		
		if (optCustomer.isEmpty()) {
			logger.error("[FINISH] - findById() WITH ERRORS");
			throw new CustomerNotFoundException("Customer not found with id: " + customerId);
		} else { 
			logger.info("[FINISH] - findById()");
			return optCustomer.get();
		}
		
	}


}
