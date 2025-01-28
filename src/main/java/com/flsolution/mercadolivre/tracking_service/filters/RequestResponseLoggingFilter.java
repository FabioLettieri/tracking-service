package com.flsolution.mercadolivre.tracking_service.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

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

	private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

	private final ObjectMapper objectMapper;
	private final RequestLogProducer requestLogProducer;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		logger.info("[START] - doFilterInternal() request: {}, response: {}", request, response);
		
		ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
		
		try {
			filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
		} finally {
			try {
				logRequestResponse(contentCachingRequestWrapper, contentCachingResponseWrapper);
			} catch (Exception ex) {
				logger.info("[FINISH] - doFilterInternal() WITH ERRORS: {}", ex.getMessage());
				ex.printStackTrace();
			}
			contentCachingResponseWrapper.copyBodyToResponse();
			logger.info("[FINISH] - doFilterInternal()");
		}
	}

	private void logRequestResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) throws Exception {
		logger.info("[START] - logRequestResponse() request: {}, response: {}", request, response);

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
		requestLogProducer.sendMessage(logMessage);
		
		logger.info("[FINISH] - logRequestResponse()");
	}
	
	private String extractBody(byte[] content) {
		logger.info("[START] - extractBody() content: {}", content);
		if (content == null || content.length == 0) {
			logger.info("[FINISH] - extractBody() WITH NULL");
			return null;
		}
		
		String response = new String(content, StandardCharsets.UTF_8);
		
		logger.info("[FINISH] - extractBody()");
		return response;
	}
	
	
	
	
}
