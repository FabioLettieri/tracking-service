package com.flsolution.mercadolivre.tracking_service.entities;

import java.time.LocalDateTime;

import com.flsolution.mercadolivre.tracking_service.enums.PackageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "packs")
public class Pack extends EntityBase {
	
	
	public Pack(
			String description, 
			String sender, 
			String recipient, 
			Boolean isHolliday, 
			String funFact,
			String estimatedDeliveryDate, 
			PackageStatus status) {
		
		this.description = description;
		this.sender = sender;
		this.recipient = recipient;
		this.isHolliday = isHolliday;
		this.funFact = funFact;
		this.estimatedDeliveryDate = estimatedDeliveryDate;
		this.status = status;
	}

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
	private PackageStatus status;
	
	@Column(nullable = true)
    private LocalDateTime deliveredAt;
	
}
