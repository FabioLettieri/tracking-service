package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
class ExternalApiTheDogServiceTest {

    private ExternalApiTheDogService externalApiTheDogService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig().usingFilesUnderDirectory("wiremock"));
        wireMockServer.start();

        RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri(wireMockServer.baseUrl())
            .build();

        externalApiTheDogService = new ExternalApiTheDogService(restTemplate);

        Field field = ExternalApiTheDogService.class.getDeclaredField("dogApiUrl");
        field.setAccessible(true);
        field.set(externalApiTheDogService, wireMockServer.baseUrl() + "/api/v2/facts?limit=1");
    }


    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetFunFactReturnsValidFact() {
        String response = externalApiTheDogService.getFunFact();

        assertEquals("Dogs have a sense of time. It's been proven that they know the difference between an hour and five.", response);
    }

    @Test
    void testGetFunFactHandlesError() {
        String errorUrl = wireMockServer.baseUrl() + "/invalid-route";
        
        try {
            Field field = ExternalApiTheDogService.class.getDeclaredField("dogApiUrl");
            field.setAccessible(true);
            field.set(externalApiTheDogService, errorUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String response = externalApiTheDogService.getFunFact();

        assertEquals("No fun fact available at the moment.", response);
    }
}
