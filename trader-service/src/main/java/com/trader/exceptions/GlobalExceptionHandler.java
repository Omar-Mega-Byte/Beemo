package com.trader.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler for the Trader Service
 * 
 * This class provides centralized exception handling across all controllers
 * in the trader-service microservice. It uses Spring's @RestControllerAdvice
 * to intercept exceptions thrown by any controller and return appropriate
 * HTTP responses with consistent error message format.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles BadRequestException (HTTP 400)
     * 
     * This method is triggered when validation fails or when the client
     * sends invalid data (e.g., empty trader name, invalid company info,
     * duplicate username/email, invalid phone number format, etc.)
     * 
     * @param e The BadRequestException containing the error message
     * @return ResponseEntity with HTTP 400 status and error message
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    /**
     * Handles NotFoundException (HTTP 404)
     * 
     * This method is triggered when a requested resource is not found
     * (e.g., trader not found, company not found, invalid trader ID, etc.)
     * 
     * @param e The NotFoundException containing the error message
     * @return ResponseEntity with HTTP 404 status and error message
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }

    /**
     * Handles all other uncaught exceptions (HTTP 500)
     * 
     * This is a fallback handler for any unexpected exceptions that are
     * not specifically handled by other methods. It prevents the application
     * from exposing sensitive error details to the client while logging
     * the actual error for debugging purposes.
     * 
     * @param e The generic Exception that was not handled elsewhere
     * @return ResponseEntity with HTTP 500 status and generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception e) {
        // TODO: Add logging here for debugging purposes
        // log.error("Unexpected error occurred in trader-service", e);
        return ResponseEntity.internalServerError().body(Map.of("message", "An unexpected error occurred"));
    }
}
