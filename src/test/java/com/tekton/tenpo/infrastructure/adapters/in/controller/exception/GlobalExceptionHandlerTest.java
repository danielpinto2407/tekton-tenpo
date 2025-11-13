package com.tekton.tenpo.infrastructure.adapters.in.controller.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleBusinessException_returnsBadRequest() {
        BusinessException exception = new BusinessException("Business logic error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Business logic error", response.getBody().message());
    }

    @Test
    void handleValidationException_returnsUnprocessableEntity() {
        ValidationException exception = new ValidationException("Invalid input");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().message());
    }

    @Test
    void handleExternalServiceException_returnsBadGateway() {
        ExternalServiceException exception = new ExternalServiceException("External API failed");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleExternalServiceException(exception);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("External API failed", response.getBody().message());
    }

    @Test
    void handleGenericException_returnsInternalServerError() {
        RuntimeException exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error", response.getBody().message());
    }

    @Test
    void errorResponse_hasAllFields() {
        ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", "Test error", "2024-01-01T00:00:00Z");
        
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.status());
        assertEquals("Bad Request", errorResponse.error());
        assertEquals("Test error", errorResponse.message());
        assertEquals("2024-01-01T00:00:00Z", errorResponse.timestamp());
    }

    @Test
    void businessException_createsWithMessage() {
        String message = "Business error";
        BusinessException exception = new BusinessException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void validationException_createsWithMessage() {
        String message = "Validation error";
        ValidationException exception = new ValidationException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void externalServiceException_createsWithMessage() {
        String message = "External service error";
        ExternalServiceException exception = new ExternalServiceException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void handleValidationErrors_combinesFieldErrors() {
        // Create two FieldErrors and package them into a BindingResult inside MethodArgumentNotValidException
        org.springframework.validation.FieldError fe1 = new org.springframework.validation.FieldError("obj", "field1", "must not be null");
        org.springframework.validation.FieldError fe2 = new org.springframework.validation.FieldError("obj", "field2", "must be a number");

        org.springframework.validation.BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(fe1);
        bindingResult.addError(fe2);

        org.springframework.web.bind.MethodArgumentNotValidException ex = new org.springframework.web.bind.MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationErrors(ex);

        assertEquals(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        // The handler joins errors using ", " so expect this concatenated message
        assertEquals("field1: must not be null, field2: must be a number", response.getBody().message());
    }

    @Test
    void handleBusinessException_trimsMessageAtNewline() {
        BusinessException exception = new BusinessException("First line\nFull stacktrace here");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(exception);

        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("First line", response.getBody().message());
    }

    @Test
    void handleGenericException_returnsDefaultMessageOnNull() {
        Exception exception = new Exception((String) null);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertEquals(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ocurrió un error inesperado. Por favor, intente nuevamente.", response.getBody().message());
    }
}
