package com.flsolution.mercadolivre.tracking_service.dtos;

public record HolidayRequestDTO (
		String date,
		String localName,
		String name,
		String countryCode,
		boolean fixed,
		boolean global,
		String type
		) {
}