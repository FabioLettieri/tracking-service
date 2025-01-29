package com.flsolution.mercadolivre.tracking_service;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TrackingServiceApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testMainMethod() {
        try (var mockSpringApplication = mockStatic(SpringApplication.class)) {
            TrackingServiceApplication.main(new String[]{});
            mockSpringApplication.verify(() -> SpringApplication.run(TrackingServiceApplication.class, new String[]{}));
        }
    }
}
