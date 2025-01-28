package com.flsolution.mercadolivre.tracking_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;
import com.flsolution.mercadolivre.tracking_service.repositories.RequestLogRepository;

@ExtendWith(MockitoExtension.class)
class RequestLogServiceTest {

    @Mock
    private RequestLogRepository requestLogRepository;

    @InjectMocks
    private RequestLogService requestLogService;

    @Test
    void testShouldCreateLogRequestSuccessfully() throws Exception {
        RequestLog requestLog = new RequestLog();
        requestLog.setMethod("POST");
        requestLog.setEndpoint("/api/v1/packs");
        requestLog.setRequestBody("{ \"description\": \"Livros para entrega\" }");
        requestLog.setResponseBody("{ \"id\": 1, \"status\": \"CREATED\" }");
        requestLog.setStatusCode(200);

        when(requestLogRepository.save(any(RequestLog.class))).thenReturn(requestLog);

        RequestLog result = requestLogService.createLogRequest(requestLog);

        assertNotNull(result);
        assertEquals("POST", result.getMethod());
        assertEquals("/api/v1/packs", result.getEndpoint());
        assertEquals("{ \"description\": \"Livros para entrega\" }", result.getRequestBody());
        assertEquals("{ \"id\": 1, \"status\": \"CREATED\" }", result.getResponseBody());
        assertEquals(200, result.getStatusCode());
    }

    @Test
    void testShouldThrowExceptionWhenSaveFails() {
        RequestLog requestLog = new RequestLog();
        requestLog.setMethod("POST");
        requestLog.setEndpoint("/api/v1/packs");
        requestLog.setRequestBody("{ \"description\": \"Livros para entrega\" }");
        requestLog.setResponseBody("{ \"id\": 1, \"status\": \"CREATED\" }");
        requestLog.setStatusCode(200);

        when(requestLogRepository.save(any(RequestLog.class))).thenThrow(new RuntimeException("Database error"));

        try {
            requestLogService.createLogRequest(requestLog);
        } catch (Exception ex) {
            assertNotNull(ex);
            assertEquals(Exception.class, ex.getClass());
        }
    }
}
