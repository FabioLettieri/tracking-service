package com.flsolution.mercadolivre.tracking_service.repositories;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;

@Repository
public interface PackEventRepository extends JpaRepository<PackEvent, Long> {
	Page<PackEvent> findByPackId(Long packId, Pageable pageable);
	Page<PackEvent> findAll(Pageable pageable);
}
