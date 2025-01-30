package com.flsolution.mercadolivre.tracking_service.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flsolution.mercadolivre.tracking_service.entities.Pack;

@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
	
	@Modifying
	@Query("UPDATE Pack p SET p.isActive = false, p.status = :newStatus WHERE p.status = :currentStatus AND p.updatedAt < :deactivateDate")
	Integer deactivateOldPackagesWithStatusInTransit(
	        @Param("deactivateDate") LocalDateTime deactivateDate,
	        @Param("currentStatus") String currentStatus,
	        @Param("newStatus") String newStatus);

	Page<Pack> findBySenderAndRecipient(String sender, String recipient, Pageable pageable);
	Page<Pack> findBySender(String sender, Pageable pageable);
	Page<Pack> findByRecipient(String recipient, Pageable pageable);
	Page<Pack> findAll(Pageable pageable);
	


}
