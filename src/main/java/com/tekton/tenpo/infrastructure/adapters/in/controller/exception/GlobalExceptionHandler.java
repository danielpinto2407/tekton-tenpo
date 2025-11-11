package com.tekton.tenpo.infrastructure.adapters.in.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 🔹 Método helper para timestamp formateado
    private String now() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

    // 🔹 Handler para errores de negocio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, cleanMessage(ex.getMessage()));
    }

    // 🔹 Handler para errores de servicios externos
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, cleanMessage(ex.getMessage()));
    }

    // 🔹 Handler para validaciones propias del dominio o aplicación
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, cleanMessage(ex.getMessage()));
    }

    // 🔹 Handler para validaciones de DTOs (Spring Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    // 🔹 Handler genérico (cualquier otra excepción no controlada)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, cleanMessage(ex.getMessage()));
    }

    // 🔹 Construcción común de respuesta de error
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse body = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                now()
        );
        return new ResponseEntity<>(body, status);
    }

    // 🔹 Método para limpiar mensajes nulos o técnicos
    private String cleanMessage(String message) {
        if (message == null || message.isBlank()) {
            return "Ocurrió un error inesperado. Por favor, intente nuevamente.";
        }
        return message.split("\n")[0]; // toma solo la primera línea, elimina trazas si existen
    }
}
