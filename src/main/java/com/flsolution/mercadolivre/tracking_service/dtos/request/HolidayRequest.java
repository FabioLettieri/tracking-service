package com.flsolution.mercadolivre.tracking_service.dtos.request;

public record HolidayRequest (
		String date,
		String localName,
		String name,
		String countryCode,
		boolean fixed,
		boolean global,
		String type
		) {
}