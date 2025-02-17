package com.flsolution.mercadolivre.tracking_service.services;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flsolution.mercadolivre.tracking_service.converters.CustomerConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.request.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Customer;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerNotFoundException;
import com.flsolution.mercadolivre.tracking_service.repositories.CustomerRepository;
import com.flsolution.mercadolivre.tracking_service.services.impl.CustomerServiceImpl;
import com.flsolution.mercadolivre.tracking_service.utils.CustomerValidation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerServiceImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	private final CustomerRepository customerRepository;
	
	@Override
	public CustomerResponse createCustomer(CustomerRequest request) {
		logger.info("[START] - createCustomer() request: {}", request);
		var optCustomer = findByDocumentOrEmail(request.document(), request.email());
		CustomerValidation.validateExistsCustomerWithDocumentOrEmail(optCustomer);
		
		var customerToCreate = Customer.builder()
				.address(request.address())
				.document(request.document())
				.email(request.email())
				.name(request.name())
				.phoneNumber(request.phoneNumber())
				.packs(new ArrayList<Pack>())
				.build();

		customerToCreate = customerRepository.save(customerToCreate);
        var response = CustomerConverter.toResponse(customerToCreate);
        
        logger.info("[FINISH] - createCustomer()");
        return response;
	}

	public Optional<Customer> findByDocumentOrEmail(String document, String email) {
		logger.info("[START] - findByDocumentAndEmail() document: {}, email: {}", document, email);
		var customer = customerRepository.findByDocumentOrEmail(document, email);
		
		logger.info("[FINISH] - findByDocumentOrEmail()");
		return customer;
	}

	public Customer findById(Long customerId) throws CustomerNotFoundException {
		logger.info("[START] - findById() customerId: {}", customerId);
		
		Optional<Customer> optCustomer = customerRepository.findById(customerId);
		var response = CustomerValidation.validateExistsCustomerWithId(optCustomer, customerId);
		
		logger.info("[FINISH] - findById()");
		return response;
		
	}

	@Override
	public Page<CustomerResponse> getCustomers(String sender, String recipient, Pageable pageable) {
		logger.info("[START] - getCustomers() sender: {}, recipient: {}, pageable: {}", sender, recipient, pageable);
		
		Page<Customer> customers = customerRepository.findAll(pageable);
		Page<CustomerResponse> response = customers.map(CustomerConverter::toResponse);
		
		logger.info("[FINISH] - getCustomers()");
		return response;
	}


}
