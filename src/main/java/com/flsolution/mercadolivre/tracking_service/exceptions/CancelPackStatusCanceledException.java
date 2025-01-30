package com.flsolution.mercadolivre.tracking_service.exceptions;

public class CancelPackStatusCanceledException extends RuntimeException {

	private static final long serialVersionUID = -6150602878788035221L;
	
	public CancelPackStatusCanceledException(String message) {
		super(message);
	}

}
