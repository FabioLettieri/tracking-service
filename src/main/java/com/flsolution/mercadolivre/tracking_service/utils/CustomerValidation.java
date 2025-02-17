package com.flsolution.mercadolivre.tracking_service.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flsolution.mercadolivre.tracking_service.entities.Customer;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerExistsWithDocumentOrEmailException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerNotFoundException;

public class CustomerValidation {
	private static final Logger logger = LoggerFactory.getLogger(CustomerValidation.class);
	
	public static Customer validateExistsCustomerWithId(Optional<Customer> optCustomer, Long customerId) throws CustomerNotFoundException {
		logger.info("[START] - validateExistsCustomerWithId() optCustomer: {}, customerId: {}", optCustomer, customerId);
		if (optCustomer.isEmpty()) {
			logger.error("[FINISH] - validateExistsCustomerWithId() WITH ERRORS");
			throw new CustomerNotFoundException("Customer not found with id: " + customerId);
		} else { 
			logger.info("[FINISH] - validateExistsCustomerWithId()");
			return optCustomer.get();
		}
	}
	
	public static void validateExistsCustomerWithDocumentOrEmail(Optional<Customer> optCustomer) {
		logger.error("[START] - validateExistsCustomerWithDocumentOrEmail() ");
		
		if (optCustomer.isPresent()) {
			logger.error("[FINISH] - validateExistsCustomerWithDocumentOrEmail() WITH ERRORS.");
			throw new CustomerExistsWithDocumentOrEmailException("The document and/or email exists, please try again with others.");
		}
		
		logger.error("[FINISH] - validateExistsCustomerWithDocumentOrEmail()");
	}
}
