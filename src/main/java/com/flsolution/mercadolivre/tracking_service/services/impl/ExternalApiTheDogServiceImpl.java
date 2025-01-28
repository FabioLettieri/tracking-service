package com.flsolution.mercadolivre.tracking_service.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalApiTheDogServiceImpl implements ExternalApiTheDogService {

	private final RestTemplate restTemplate;
	
	@Value("${external-apis.dog-api-url}")
	private String dogApiUrl;
	
	@Override
	public String getFunFact() {
		try {
			return restTemplate.getForObject(dogApiUrl, String.class);
		} catch (Exception ex) {
			return "No fun fact available at the moment.";
		}
	}

}
