package com.flsolution.mercadolivre.tracking_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flsolution.mercadolivre.tracking_service.entities.Pack;

public interface PackRepository extends JpaRepository<Pack, Long> {

}
