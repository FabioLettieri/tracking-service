package com.flsolution.mercadolivre.tracking_service.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flsolution.mercadolivre.tracking_service.entities.RequestLog;
import com.flsolution.mercadolivre.tracking_service.producers.RequestLogProducer;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;
	private final RequestLogProducer requestLogProducer;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
		
		try {
			filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
		} finally {
			logRequestResponse(contentCachingRequestWrapper, contentCachingResponseWrapper);
			contentCachingResponseWrapper.copyBodyToResponse();
		}
	}

	private void logRequestResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) throws JsonProcessingException {
		String requestBody = extractBody(request.getContentAsByteArray());
		String responseBody = extractBody(response.getContentAsByteArray());
		
		RequestLog requestlog = RequestLog.builder()
				.method(request.getMethod())
				.endpoint(request.getRequestURI())
				.requestBody(requestBody)
				.responseBody(responseBody)
				.statusCode(response.getStatus())
				.build();
		
		String logMessage = objectMapper.writeValueAsString(requestlog);
		requestLogProducer.sendRequestLog(logMessage);
	}
	
	private String extractBody(byte[] content) {
		if (content == null || content.length == 0) {
			return null;
		}
		
		return new String(content, StandardCharsets.UTF_8);
	}
	
	
	
	
}
