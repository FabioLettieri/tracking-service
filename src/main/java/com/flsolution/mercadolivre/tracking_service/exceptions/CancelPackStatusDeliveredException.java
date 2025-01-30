package com.flsolution.mercadolivre.tracking_service.exceptions;

public class CancelPackStatusDeliveredException extends RuntimeException {

	private static final long serialVersionUID = -4133962194633434467L;
	
	public CancelPackStatusDeliveredException(String message) {
		super(message);
	}

}
