package com.flsolution.mercadolivre.tracking_service.batch;

public class ProcessorEventException extends RuntimeException {

	private static final long serialVersionUID = -3750071521732092269L;

	public ProcessorEventException(String message) {
		super(message);
	}
}
