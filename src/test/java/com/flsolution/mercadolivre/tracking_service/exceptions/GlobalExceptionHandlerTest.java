package com.flsolution.mercadolivre.tracking_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.flsolution.mercadolivre.tracking_service.batch.ProcessorEventException;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    // ✅ Teste para exceção genérica (RuntimeException)
    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Erro inesperado");

        ResponseEntity<String> response = exceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Erro inesperado", response.getBody());
    }

    // ✅ Teste para erro quando o pacote não é encontrado
    @Test
    void testHandlePackNotFoundException() {
        PackNotFoundException ex = new PackNotFoundException("Pacote não encontrado");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handlePackNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pacote não encontrado", response.getBody().get("error"));
    }

    // ✅ Teste para erro ao processar evento
    @Test
    void testHandleProcessorEventException() {
        ProcessorEventException ex = new ProcessorEventException("Erro ao processar evento");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleProcessorEventException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao processar evento", response.getBody().get("error"));
    }

    // ✅ Teste para erro ao tentar cancelar um pacote em trânsito
    @Test
    void testHandleCancelPackStatusInTransitException() {
        CancelPackStatusInTransitException ex = new CancelPackStatusInTransitException("Pacote em trânsito");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCancelPackStatusInTransitException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pacote em trânsito", response.getBody().get("error"));
    }

    // ✅ Teste para erro ao tentar cancelar um pacote já cancelado
    @Test
    void testHandleCancelPackStatusCanceledException() {
        CancelPackStatusCanceledException ex = new CancelPackStatusCanceledException("Pacote já cancelado");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCancelPackStatusCanceledException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pacote já cancelado", response.getBody().get("error"));
    }

    // ✅ Teste para erro ao tentar cancelar um pacote já entregue
    @Test
    void testHandleCancelPackStatusDeliveredException() {
        CancelPackStatusDeliveredException ex = new CancelPackStatusDeliveredException("Pacote já entregue");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCancelPackStatusDeliveredException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pacote já entregue", response.getBody().get("error"));
    }

    // ✅ Teste para erro de formato inválido (HttpMessageNotReadableException)
    @Test
    void testHandleInvalidFormatException() {
        // Criando uma exceção simulada com um campo inválido
        List<Reference> path = new LinkedList<>();
        path.add(new Reference(null, "campoInvalido"));

        InvalidFormatException invalidFormatException = new InvalidFormatException(null, "Valor inválido", "123", String.class);
        invalidFormatException.prependPath(path.get(0));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro de formatação", invalidFormatException);

        ResponseEntity<Object> response = exceptionHandler.handleInvalidFormatException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request format", ((Map<?, ?>) response.getBody()).get("error"));
        assertEquals("O campo 'campoInvalido' recebeu: 123. Mas espera um tipo diferente, favor corrigir!", ((Map<?, ?>) response.getBody()).get("message"));
    }

    // ✅ Teste para erro genérico de formatação inválida (quando não há campo específico)
    @Test
    void testHandleInvalidFormatException_generic() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro genérico");

        ResponseEntity<Object> response = exceptionHandler.handleInvalidFormatException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request format", ((Map<?, ?>) response.getBody()).get("error"));
        assertEquals("Erro ao processar a requisição.", ((Map<?, ?>) response.getBody()).get("message"));
    }
}
