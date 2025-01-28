package com.flsolution.mercadolivre.tracking_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "request_logs")
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
