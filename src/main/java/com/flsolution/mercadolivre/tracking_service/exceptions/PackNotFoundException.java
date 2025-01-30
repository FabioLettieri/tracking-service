package com.flsolution.mercadolivre.tracking_service.exceptions;

public class PackNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1743812502297941680L;

	public PackNotFoundException(String message) {
		super(message);
	}

}
