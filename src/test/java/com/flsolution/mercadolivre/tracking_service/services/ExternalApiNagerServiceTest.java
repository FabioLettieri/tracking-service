package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "spring.rabbitmq.listener.simple.auto-startup=false"
})
class ExternalApiNagerServiceTest {

    private ExternalApiNagerService externalApiNagerService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
            .usingFilesUnderDirectory("wiremock")); // Aponta para mocks
        wireMockServer.start();

        String nagerDateUrl = wireMockServer.baseUrl();
        
        RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri(nagerDateUrl)  // Garante que o RestTemplate use o WireMock
            .build();

        externalApiNagerService = new ExternalApiNagerService(restTemplate);
        
     // Sobrescreve a variável privada `nagerDateUrl`
        Field field = ExternalApiNagerService.class.getDeclaredField("nagerDateUrl");
        field.setAccessible(true);
        field.set(externalApiNagerService, wireMockServer.baseUrl());  // Aplica a URL correta
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testIsHolidayReturnsTrue() {
        String testDate = "2025-12-25";
        Boolean result = externalApiNagerService.isHolliday(testDate);

        assertTrue(result);
    }

    @Test
    void testIsHolidayReturnsFalse() {
        // Chama o método isHolliday para uma data mockada no arquivo de mapping
        String testDate = "2025-12-26";
        Boolean result = externalApiNagerService.isHolliday(testDate);

        // Verifica se o resultado é false
        assertFalse(result);
    }

    @Test
    void testIsHolidayHandlesException() {
        // Chama o método isHolliday para uma data mockada no arquivo de mapping
        String testDate = "2025-12-27";
        Boolean result = externalApiNagerService.isHolliday(testDate);

        // Verifica se o resultado é false devido ao erro
        assertFalse(result);
    }
}
