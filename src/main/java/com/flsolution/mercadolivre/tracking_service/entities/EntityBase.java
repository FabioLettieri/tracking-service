package com.flsolution.mercadolivre.tracking_service.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class EntityBase implements Serializable {

	private static final long serialVersionUID = -6332486979595010787L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private Boolean isActive = true;
	
	@PrePersist
	private void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	private void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	
}
