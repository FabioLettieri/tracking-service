package com.flsolution.mercadolivre.tracking_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
	    "spring.rabbitmq.listener.simple.auto-startup=false"
	})
class TrackingServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
