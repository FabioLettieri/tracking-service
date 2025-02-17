package com.flsolution.mercadolivre.tracking_service.exceptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * Handle generic runtime exceptions.
     */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
		logger.error("[ERROR] - Exception occurred: {}", ex.getMessage(), ex);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An unexpected error occurred: " + ex.getMessage());
	}
	
	/**
     * Handle validation errors and return a 400 Bad Request with detailed messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("[ERROR] - Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handle validation errors and return a 404 Not Found with detailed messages.
     */
    @ExceptionHandler(PackNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePackNotFoundException(PackNotFoundException ex) {
    	logger.error("[ERROR] - Pack not found: {}", ex.getMessage());
    	
    	Map<String, String> error = Collections.singletonMap("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
	
	/**
     * Handle errors and return a 400 Bad Request with detailed messages.
     */
    @ExceptionHandler(ProcessorEventException.class)
    public ResponseEntity<Map<String, String>> handleProcessorEventException(ProcessorEventException ex) {
    	logger.error("[ERROR] - The message came up with an invalid field: {}", ex.getMessage());

        Map<String, String> error = Collections.singletonMap("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle errors and return a 400 Bad Request with detailed messages.
     */
    @ExceptionHandler(CancelPackStatusInTransitException.class)
    public ResponseEntity<Map<String, String>> handleCancelPackStatusInTransitException(CancelPackStatusInTransitException ex) {
    	logger.error("[ERROR] - Order in transit, action refused: {}", ex.getMessage());
    	
    	Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * Handle errors and return a 400 Bad Request with detailed messages.
     */
    @ExceptionHandler(CancelPackStatusCanceledException.class)
    public ResponseEntity<Map<String, String>> handleCancelPackStatusCanceledException(CancelPackStatusCanceledException ex) {
    	logger.error("[ERROR] - Action not taken, as it has already been canceled: {}", ex.getMessage());
    	
    	Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle errors and return a 400 Bad Request with detailed messages.
     */
    @ExceptionHandler(CancelPackStatusDeliveredException.class)
    public ResponseEntity<Map<String, String>> handleCancelPackStatusDeliveredException(CancelPackStatusDeliveredException ex) {
    	logger.error("[ERROR] - Action not carried out, since it has already been delivered: {}", ex.getMessage());
    	
    	Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    /**
     * Handle errors and return a 400 Bad Request with detailed messages.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidFormatException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().get(0).getFieldName();
            String invalidValue = invalidFormatException.getValue().toString();
            
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("error", "Invalid request format");
            errorResponse.put("message", "O campo '" + fieldName + "' recebeu: " + invalidValue + ". Mas espera um tipo diferente, favor corrigir!");

            return ResponseEntity.badRequest().body(errorResponse);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
            "status", HttpStatus.BAD_REQUEST.value(),
            "error", "Invalid request format",
            "message", "Erro ao processar a requisição."
        ));
    }
    
    /**
     * Handle errors and return a 404 Not Found with detailed messages.
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
    	logger.error("[ERROR] - Customer not found: {}", ex.getMessage());
    	
    	Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle errors and return a 400 Bad Request with detailed messages.
     */
    @ExceptionHandler(CustomerExistsWithDocumentOrEmailException.class)
    public ResponseEntity<Map<String, String>> handleCustomerExistsWithDocumentOrEmailException(CustomerExistsWithDocumentOrEmailException ex) {
    	logger.error("[ERROR] - Customer already exists with document and/or email: {}", ex.getMessage());
    	
    	Map<String, String> error = Collections.singletonMap("error", ex.getMessage());
    	
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
