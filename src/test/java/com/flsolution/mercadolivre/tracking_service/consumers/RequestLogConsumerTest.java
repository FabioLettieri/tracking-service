package com.flsolution.mercadolivre.tracking_service.consumers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;
import com.flsolution.mercadolivre.tracking_service.services.RequestLogService;

@ExtendWith(MockitoExtension.class)
class RequestLogConsumerTest {

    @Mock
    private RequestLogService requestLogService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RequestLogConsumer requestLogConsumer;

    @Test
    void testConsumeRequestLogSuccessfully() throws Exception {
        String message = "{\"id\":1,\"createdAt\":null,\"updatedAt\":null,\"isActive\":true,\"method\":\"POST\",\"endpoint\":\"/api/v1/packs\",\"requestBody\":\"{\\\"description\\\":\\\"Livros\\\"}\",\"responseBody\":\"{}\",\"statusCode\":200}";
        RequestLog requestLog = new RequestLog();
        requestLog.setId(1L);
        requestLog.setMethod("POST");
        requestLog.setEndpoint("/api/v1/packs");

        when(objectMapper.readValue(message, RequestLog.class)).thenReturn(requestLog);

        requestLogConsumer.consumeRequestLog(message);

        verify(requestLogService, times(1)).createLogRequest(any(RequestLog.class));
    }

    @Test
    void testConsumeRequestLogWithException() throws Exception {
        String message = "invalid message";

        when(objectMapper.readValue(message, RequestLog.class)).thenThrow(new RuntimeException("Erro de parsing"));

        requestLogConsumer.consumeRequestLog(message);

        verify(requestLogService, never()).createLogRequest(any(RequestLog.class));
    }
}
