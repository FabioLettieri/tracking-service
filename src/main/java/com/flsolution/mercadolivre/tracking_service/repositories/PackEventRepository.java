package com.flsolution.mercadolivre.tracking_service.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flsolution.mercadolivre.tracking_service.entities.PackEvent;

@Repository
public interface PackEventRepository extends JpaRepository<PackEvent, Long> {
	List<PackEvent> findByPackId(Long packId);
	List<PackEvent> findBySenderAndRecipient(String sender, String recipient);
	List<PackEvent> findBySender(String sender);
	List<PackEvent> findByRecipient(String recipient);
}
