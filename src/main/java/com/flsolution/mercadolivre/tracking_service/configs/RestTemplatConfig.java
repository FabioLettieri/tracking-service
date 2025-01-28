package com.flsolution.mercadolivre.tracking_service.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplatConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
