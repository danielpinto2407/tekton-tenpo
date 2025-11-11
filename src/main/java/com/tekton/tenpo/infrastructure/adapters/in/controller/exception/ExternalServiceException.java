package com.tekton.tenpo.infrastructure.adapters.in.controller.exception;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
}