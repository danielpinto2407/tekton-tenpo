package com.tekton.tenpo.infrastructure.adapters.in.controller.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
