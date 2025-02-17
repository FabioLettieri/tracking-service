package com.flsolution.mercadolivre.tracking_service.exceptions;

public class PackStatusInvalidException extends RuntimeException {

	private static final long serialVersionUID = 8981078352212534303L;

	public PackStatusInvalidException(String message) {
		super(message);
	}
	
}
