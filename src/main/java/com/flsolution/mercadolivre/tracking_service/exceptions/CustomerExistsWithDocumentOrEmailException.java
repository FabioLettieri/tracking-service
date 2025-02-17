package com.flsolution.mercadolivre.tracking_service.exceptions;

public class CustomerExistsWithDocumentOrEmailException extends RuntimeException {

	private static final long serialVersionUID = 4664746907473213872L;
	
	public CustomerExistsWithDocumentOrEmailException(String message) {
		super(message);
	}

}
