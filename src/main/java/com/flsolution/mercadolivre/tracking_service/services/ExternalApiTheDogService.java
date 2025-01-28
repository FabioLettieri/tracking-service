package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flsolution.mercadolivre.tracking_service.dtos.DogApiResponseDTO;
import com.flsolution.mercadolivre.tracking_service.services.impl.ExternalApiTheDogServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalApiTheDogService implements ExternalApiTheDogServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ExternalApiTheDogService.class);
	private final RestTemplate restTemplate;
	
	@Value("${external-apis.dog-api-url}")
	private String dogApiUrl;
	
	@Override
	public String getFunFact() {
		try {
			logger.info("[START] - getFunFact()");
			
			DogApiResponseDTO responseDogApi = restTemplate.getForObject(dogApiUrl, DogApiResponseDTO.class);
            String response = responseDogApi.getData().get(0).getAttributes().getBody();
			
			logger.info("[FINISH] - getFunFact()");
			
			return response;
		} catch (Exception ex) {
			logger.info("[FINISH] - getFunFact() WITH ERRORS: {}", ex.getMessage());
			return "No fun fact available at the moment.";
		}
	}

}
