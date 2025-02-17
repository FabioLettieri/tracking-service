package com.flsolution.mercadolivre.tracking_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flsolution.mercadolivre.tracking_service.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByDocumentOrEmail(String document, String email);

}
