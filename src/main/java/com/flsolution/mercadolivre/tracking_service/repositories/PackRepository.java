package com.flsolution.mercadolivre.tracking_service.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

	@Query("SELECT p FROM Pack p WHERE p.sender = :sender AND p.recipient = :recipient")
    Page<Pack> findBySenderAndRecipient(
            @Param("sender") String sender, 
            @Param("recipient") String recipient, 
            Pageable pageable
    );

    @Query("SELECT p FROM Pack p WHERE p.sender = :sender")
    Page<Pack> findBySender(
            @Param("sender") String sender, 
            Pageable pageable
    );

    @Query("SELECT p FROM Pack p WHERE p.recipient = :recipient")
    Page<Pack> findByRecipient(
            @Param("recipient") String recipient, 
            Pageable pageable
    );

    @Query("SELECT p FROM Pack p")
    Page<Pack> findAll(Pageable pageable);

	Optional<List<Pack>> findByCustomerId(Long customerId);
	


}
