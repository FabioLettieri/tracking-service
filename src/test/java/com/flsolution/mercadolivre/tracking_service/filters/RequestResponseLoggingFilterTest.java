package com.flsolution.mercadolivre.tracking_service.filters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;
import com.flsolution.mercadolivre.tracking_service.producers.RequestLogProducer;

import jakarta.servlet.ServletException;

@ExtendWith(MockitoExtension.class)
class RequestResponseLoggingFilterTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RequestLogProducer requestLogProducer;

    @InjectMocks
    private RequestResponseLoggingFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        lenient().doNothing().when(requestLogProducer).sendMessage(any(String.class));
    }

    @Test
    void testDoFilterInternal() throws ServletException, IOException, Exception {
        request.setMethod("POST");
        request.setRequestURI("/api/v1/test");
        request.setContent("{\"key\":\"value\"}".getBytes());

        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().write("{\"response\":\"success\"}");
        response.getWriter().flush();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String expectedRequestLogJson = "{\"method\":\"POST\",\"endpoint\":\"/api/v1/test\",\"requestBody\":\"{\\\"key\\\":\\\"value\\\"}\",\"responseBody\":\"{\\\"response\\\":\\\"success\\\"}\",\"statusCode\":200}";

        when(objectMapper.writeValueAsString(any(RequestLog.class))).thenReturn(expectedRequestLogJson);

        filter.doFilterInternal(wrappedRequest, wrappedResponse, filterChain);

        wrappedResponse.copyBodyToResponse();

        verify(requestLogProducer).sendMessage(eq(expectedRequestLogJson));
    }

}
