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

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Erro inesperado");

        ResponseEntity<String> response = exceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Erro inesperado", response.getBody());
    }

    @Test
    void testHandlePackNotFoundException() {
        PackNotFoundException ex = new PackNotFoundException("Pacote não encontrado");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handlePackNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pacote não encontrado", response.getBody().get("error"));
    }

    @Test
    void testHandleProcessorEventException() {
        ProcessorEventException ex = new ProcessorEventException("Erro ao processar evento");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleProcessorEventException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro ao processar evento", response.getBody().get("error"));
    }

    @Test
    void testHandleCancelPackStatusInTransitException() {
        CancelPackStatusInTransitException ex = new CancelPackStatusInTransitException("Pacote em trânsito");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCancelPackStatusInTransitException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pacote em trânsito", response.getBody().get("error"));
    }

    @Test
    void testHandleCancelPackStatusCanceledException() {
        CancelPackStatusCanceledException ex = new CancelPackStatusCanceledException("Pacote já cancelado");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCancelPackStatusCanceledException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pacote já cancelado", response.getBody().get("error"));
    }

    @Test
    void testHandleCancelPackStatusDeliveredException() {
        CancelPackStatusDeliveredException ex = new CancelPackStatusDeliveredException("Pacote já entregue");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCancelPackStatusDeliveredException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pacote já entregue", response.getBody().get("error"));
    }

    @Test
    void testHandleInvalidFormatException() {
        List<Reference> path = new LinkedList<>();
        path.add(new Reference(null, "campoInvalido"));

        InvalidFormatException invalidFormatException = new InvalidFormatException(null, "Valor inválido", "123", String.class);
        invalidFormatException.prependPath(path.get(0));

        @SuppressWarnings("deprecation")
		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro de formatação", invalidFormatException);

        ResponseEntity<Object> response = exceptionHandler.handleInvalidFormatException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request format", ((Map<?, ?>) response.getBody()).get("error"));
        assertEquals("O campo 'campoInvalido' recebeu: 123. Mas espera um tipo diferente, favor corrigir!", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void testHandleInvalidFormatException_generic() {
        @SuppressWarnings("deprecation")
		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro genérico");

        ResponseEntity<Object> response = exceptionHandler.handleInvalidFormatException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid request format", ((Map<?, ?>) response.getBody()).get("error"));
        assertEquals("Erro ao processar a requisição.", ((Map<?, ?>) response.getBody()).get("message"));
    }
    
    @Test
    void testHandleCustomerNotFoundException() {
    	CustomerNotFoundException ex = new CustomerNotFoundException("Cliente não encontrado.");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCustomerNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cliente não encontrado.", response.getBody().get("error"));
    }
    
    @Test
    void testHandleCustomerExistsWithDocumentOrEmailException() {
    	CustomerExistsWithDocumentOrEmailException ex = new CustomerExistsWithDocumentOrEmailException("Documento e/ou email ja estão registrados.");

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleCustomerExistsWithDocumentOrEmailException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Documento e/ou email ja estão registrados.", response.getBody().get("error"));
    }
    
    @Test
    void testHandlePackCreateDuplicateDetectedException() {
    	PackCreateDuplicateDetectedException ex = new PackCreateDuplicateDetectedException("Requisição duplicada foi detectada, criação do Pack foi descartada.");
    	
    	ResponseEntity<Map<String, String>> response = exceptionHandler.handlePackCreateDuplicateDetected(ex);
    	
    	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    	assertEquals("Requisição duplicada foi detectada, criação do Pack foi descartada.", response.getBody().get("error"));
    }
    
    
    
}
