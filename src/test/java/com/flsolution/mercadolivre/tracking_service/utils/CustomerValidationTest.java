package com.flsolution.mercadolivre.tracking_service.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.flsolution.mercadolivre.tracking_service.entities.Customer;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerExistsWithDocumentOrEmailException;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerNotFoundException;

class CustomerValidationTest {

	@Test
    void testValidateExistsCustomerWithId_customerExists() {
        Customer customer = new Customer();
        Optional<Customer> optCustomer = Optional.of(customer);
        Long customerId = 1L;

        assertDoesNotThrow(() -> CustomerValidation.validateExistsCustomerWithId(optCustomer, customerId));
    }

    @Test
    void testValidateExistsCustomerWithId_customerNotFound() {
        Optional<Customer> optCustomer = Optional.empty();
        Long customerId = 1L;

        Exception exception = assertThrows(CustomerNotFoundException.class, 
            () -> CustomerValidation.validateExistsCustomerWithId(optCustomer, customerId));

        assertEquals("Customer not found with id: " + customerId, exception.getMessage());
    }

    @Test
    void testValidateExistsCustomerWithDocumentOrEmail_customerExists() {
        Customer customer = new Customer();
        Optional<Customer> optCustomer = Optional.of(customer);

        Exception exception = assertThrows(CustomerExistsWithDocumentOrEmailException.class, 
            () -> CustomerValidation.validateExistsCustomerWithDocumentOrEmail(optCustomer));

        assertEquals("The document and/or email exists, please try again with others.", exception.getMessage());
    }

    @Test
    void testValidateExistsCustomerWithDocumentOrEmail_customerDoesNotExist() {
        Optional<Customer> optCustomer = Optional.empty();

        assertDoesNotThrow(() -> CustomerValidation.validateExistsCustomerWithDocumentOrEmail(optCustomer));
	}

}
