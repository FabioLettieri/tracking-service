package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.flsolution.mercadolivre.tracking_service.converters.CustomerConverter;
import com.flsolution.mercadolivre.tracking_service.dtos.request.CustomerRequest;
import com.flsolution.mercadolivre.tracking_service.dtos.response.CustomerResponse;
import com.flsolution.mercadolivre.tracking_service.entities.Customer;
import com.flsolution.mercadolivre.tracking_service.entities.Pack;
import com.flsolution.mercadolivre.tracking_service.exceptions.CustomerNotFoundException;
import com.flsolution.mercadolivre.tracking_service.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	private CustomerRequest customerRequest;
	private Customer customer;
	private CustomerResponse customerResponse;

	@BeforeEach
	void setUp() {
		customerRequest = new CustomerRequest("name", "12345678900", "11912345678", "email@email.com", "address");
		customer = Customer.builder().address("address").document("document").email("email@email.com").username("name")
				.phoneNumber("phoneNumber").packs(new ArrayList<Pack>()).build();
		customerResponse = CustomerConverter.toResponse(customer);
	}

	@Test
	void testCreateCustomer() {
		when(customerRepository.findByDocumentOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		CustomerResponse response = customerService.createCustomer(customerRequest);

		assertNotNull(response);
		assertEquals(customerResponse.email(), response.email());
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	@Test
	void testFindByDocumentOrEmail() {
		when(customerRepository.findByDocumentOrEmail(customer.getDocument(), customer.getEmail()))
				.thenReturn(Optional.of(customer));

		Optional<Customer> foundCustomer = customerService.findByDocumentOrEmail(customer.getDocument(),
				customer.getEmail());

		assertTrue(foundCustomer.isPresent());
		assertEquals(customer.getEmail(), foundCustomer.get().getEmail());
	}

	@Test
	void testFindById() throws CustomerNotFoundException {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

		Customer foundCustomer = customerService.findById(1L);

		assertNotNull(foundCustomer);
		assertEquals(customer.getEmail(), foundCustomer.getEmail());
	}

	@Test
	void testGetCustomers() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Customer> page = new PageImpl<>(Collections.singletonList(customer), pageable, 1);
		when(customerRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

		Page<CustomerResponse> customersPage = customerService.getCustomers("sender", "recipient", pageable);

		assertNotNull(customersPage);
		assertEquals(1, customersPage.getTotalElements());
	}

}
