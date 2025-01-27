package com.flsolution.mercadolivre.tracking_service.entities;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "packs")
public class Pack extends EntityBase {

	private static final long serialVersionUID = 7447140689941622598L;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private String sender;
	
	@Column(nullable = false)
	private String recipient;
	
	private Boolean isHolliday;
	
	@Column(nullable = true)
	private String funFact;
	
	@Column(nullable = true)
	private String estimatedDeliveryDate;
	
	@Enumerated(EnumType.STRING)
	private PackageStatus status = PackageStatus.CREATED;
	
}
