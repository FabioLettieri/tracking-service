package com.flsolution.mercadolivre.tracking_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.flsolution.mercadolivre.tracking_service.dtos.response.DogApiResponse;
import com.flsolution.mercadolivre.tracking_service.services.impl.ExternalApiTheDogServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExternalApiTheDogService implements ExternalApiTheDogServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ExternalApiTheDogService.class);
	private final RestTemplate restTemplate;
	
	@Value("${external-apis.dog-api-url}")
	private String dogApiUrl;
	
	@Override
	@CircuitBreaker(name = "trackingService", fallbackMethod = "fallbackResponse")
	public String getFunFact() {
		try {
			logger.info("[START] - getFunFact()");
			
			DogApiResponse responseDogApi = restTemplate.getForObject(dogApiUrl, DogApiResponse.class);
            String response = responseDogApi.data().get(0).attributes().body();
			
			logger.info("[FINISH] - getFunFact()");
			return response;
		} catch (Exception ex) {
			logger.info("[FINISH] - getFunFact() WITH ERRORS: {}", ex.getMessage());
			return "No fun fact available at the moment.";
		}
	}
	
	public ResponseEntity<String> fallbackResponse(String url, Throwable t) {
		logger.error("[START] - fallbackResponse()");
		ResponseEntity<String> response = ResponseEntity.status(503).body("Serviço ExternalApiTheDogService temporariamente indisponível.");
		
		logger.error("[FINISH] - fallbackResponse() WITH ERRORS: {}", response.getBody());
        return response;
    }

}
