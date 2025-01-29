package com.flsolution.mercadolivre.tracking_service.filters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

import com.fasterxml.jackson.core.JsonProcessingException;
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
    void testDoFilterInternal_Success() throws ServletException, IOException, Exception {
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

    @SuppressWarnings("serial")
	@Test
    void testDoFilterInternal_WhenExceptionOccursDuringLogging_ShouldHandleGracefully() throws Exception {
        request.setMethod("GET");
        request.setRequestURI("/api/v1/fail");

        response.setStatus(500);
        response.getWriter().write("{\"error\":\"failure\"}");
        response.getWriter().flush();

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        doThrow(new JsonProcessingException("Simulated error") {}).when(objectMapper).writeValueAsString(any(RequestLog.class));

        filter.doFilterInternal(wrappedRequest, wrappedResponse, filterChain);

        verify(requestLogProducer, never()).sendMessage(any());
    }

    @Test
    void testLogRequestResponse_WithValidRequestResponse() throws Exception {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        when(objectMapper.writeValueAsString(any(RequestLog.class))).thenReturn("{}");

        filter.doFilterInternal(wrappedRequest, wrappedResponse, filterChain);

        verify(requestLogProducer, times(1)).sendMessage(any());
    }

    @Test
    void testExtractBody_WithValidContent() {
        byte[] content = "{\"key\":\"value\"}".getBytes();
        String result = filter.extractBody(content);
        assertEquals("{\"key\":\"value\"}", result);
    }

    @Test
    void testExtractBody_WithEmptyContent_ShouldReturnNull() {
        byte[] content = {};
        String result = filter.extractBody(content);
        assertEquals(null, result);
    }
}
