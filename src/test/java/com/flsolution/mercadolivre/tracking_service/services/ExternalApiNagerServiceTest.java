package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

@SpringBootTest
class ExternalApiNagerServiceTest {

    private ExternalApiNagerService externalApiNagerService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        wireMockServer = new WireMockServer(
        		WireMockConfiguration.wireMockConfig().usingFilesUnderDirectory("wiremock"));
        wireMockServer.start();

        RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri(wireMockServer.baseUrl())
            .build();

        externalApiNagerService = new ExternalApiNagerService(restTemplate);	

        Field field = ExternalApiNagerService.class.getDeclaredField("nagerDateUrl");
        field.setAccessible(true);
        field.set(externalApiNagerService, wireMockServer.baseUrl() + "/api/v3/publicholidays");
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testIsHolidayReturnsTrue() {
        String testDate = "2025-12-25";
        Boolean result = externalApiNagerService.isHoliday(testDate);

        assertTrue(result);
    }

    @Test
    void testIsHolidayReturnsFalse() {
        String testDate = "2025-12-26";
        Boolean result = externalApiNagerService.isHoliday(testDate);

        assertFalse(result);
    }

    @Test
    void testIsHolidayHandlesException() {
    	
    	String errorUrl = wireMockServer.baseUrl() + "/invalid-route";
    	
    	try {
            Field field = ExternalApiTheDogService.class.getDeclaredField("nagerDateUrl");
            field.setAccessible(true);
            field.set(externalApiNagerService, errorUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	String testDate = "2025-12-27";
    	
    	Boolean response = externalApiNagerService.isHoliday(testDate);

        assertFalse(response);

    }
}
