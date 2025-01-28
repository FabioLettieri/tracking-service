package com.flsolution.mercadolivre.tracking_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "request_logs")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestLog extends EntityBase {

	private static final long serialVersionUID = 197763159314719576L;

	@Column(nullable = false)
	private String method;
	
	@Column(nullable = false)
	private String endpoint;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String requestBody;
	
	@Column(nullable = true, columnDefinition = "TEXT")
	private String responseBody;
	
	@Column(nullable = false)
	private Integer statusCode;
	
}
