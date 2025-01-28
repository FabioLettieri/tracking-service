package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
class ExternalApiTheDogServiceTest {

    private ExternalApiTheDogService externalApiTheDogService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        wireMockServer = new WireMockServer(
            WireMockConfiguration.wireMockConfig().usingFilesUnderDirectory("wiremock"));
        wireMockServer.start();

        // Configura o RestTemplate apontando para o WireMock
        RestTemplate restTemplate = new RestTemplateBuilder()
            .rootUri(wireMockServer.baseUrl())
            .build();

        externalApiTheDogService = new ExternalApiTheDogService(restTemplate);

        // Sobrescreve o valor da URL da API com a URL do WireMock
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
        // Chama o método que será testado
        String response = externalApiTheDogService.getFunFact();

        // Valida o retorno esperado
        assertEquals("Dogs have a sense of time. It's been proven that they know the difference between an hour and five.", response);
    }

    @Test
    void testGetFunFactHandlesError() {
        // Para forçar um erro, podemos simular uma rota inexistente
        String errorUrl = wireMockServer.baseUrl() + "/invalid-route";
        
        // Sobrescreve a URL para testar o erro
        try {
            Field field = ExternalApiTheDogService.class.getDeclaredField("dogApiUrl");
            field.setAccessible(true);
            field.set(externalApiTheDogService, errorUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chama o método que será testado
        String response = externalApiTheDogService.getFunFact();

        // Valida o retorno padrão em caso de erro
        assertEquals("No fun fact available at the moment.", response);
    }
}
