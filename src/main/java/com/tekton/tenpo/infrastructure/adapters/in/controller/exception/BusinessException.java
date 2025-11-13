package com.tekton.tenpo.infrastructure.adapters.in.controller.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}