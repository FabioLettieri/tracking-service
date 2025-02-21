package com.flsolution.mercadolivre.tracking_service.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplatConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(RestTemplatConfig.class);

	@Bean
	RestTemplate restTemplate() {
		logger.info("[START] - restTemplate()");
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
		
        RestTemplate response = new RestTemplate(factory);
		
		logger.info("[FINISH] - restTemplate()");
		return response;
	}
}
