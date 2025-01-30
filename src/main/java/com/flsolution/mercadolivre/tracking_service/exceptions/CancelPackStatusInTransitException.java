package com.flsolution.mercadolivre.tracking_service.exceptions;

public class CancelPackStatusInTransitException extends RuntimeException {

	private static final long serialVersionUID = 236639836679667894L;

	public CancelPackStatusInTransitException(String message) {
		super(message);
	}
	
	
}
