package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalApiNagerServiceImpl implements ExternalApiNagerService {

	private final RestTemplate restTemplate;
	
	@Value("${external-apis.nager-date-url}")
	private String nagerDateUrl;
	
	@Override
	public Boolean isHolliday(String date) {
		String url = String.format("%s/%s", nagerDateUrl, date);
		try {
			Boolean response = restTemplate.getForObject(url, Boolean.class);
			return response != null && response;
		} catch (Exception ex) {
			return false;
		}
	}

}
