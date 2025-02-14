package com.flsolution.mercadolivre.tracking_service.exceptions;

public class CustomerNotFoundException extends Exception {

	private static final long serialVersionUID = 4015758697873325918L;

	public CustomerNotFoundException(String message) {
		super(message);
	}
}
