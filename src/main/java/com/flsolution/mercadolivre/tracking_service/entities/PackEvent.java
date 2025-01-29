package com.flsolution.mercadolivre.tracking_service.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "pack_events")
public class PackEvent extends EntityBase {

	private static final long serialVersionUID = -4139032495310327294L;
	
	@Column(nullable = false)
	private String location;
	
	@Column(nullable = false)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "pack_id", nullable = false)
	private Pack pack;
	
	@Column(name = "sender", nullable = false)
	private String sender;
	
	@Column(name = "recipient", nullable = false)
	private String recipient;
	
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	private LocalDateTime eventDateTime;
}
