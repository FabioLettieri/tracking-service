package com.flsolution.mercadolivre.tracking_service.exceptions;

public class PackCreateDuplicateDetected extends Exception {

	private static final long serialVersionUID = -5759867021932748273L;
	
	public PackCreateDuplicateDetected(String message) {
		super(message);
	}

}
