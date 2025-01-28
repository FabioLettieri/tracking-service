package com.flsolution.mercadolivre.tracking_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flsolution.mercadolivre.tracking_service.entities.Pack;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {

}
