package com.flsolution.mercadolivre.tracking_service.exceptions;

public class PackCreateDuplicateDetectedException extends Exception {

	private static final long serialVersionUID = -5759867021932748273L;
	
	public PackCreateDuplicateDetectedException(String message) {
		super(message);
	}

}
