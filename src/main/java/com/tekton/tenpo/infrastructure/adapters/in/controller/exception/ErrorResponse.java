package com.tekton.tenpo.infrastructure.adapters.in.controller.exception;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String timestamp
) {}
