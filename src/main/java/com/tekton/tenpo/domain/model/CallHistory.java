package com.tekton.tenpo.domain.model;

import java.time.Instant;
public record CallHistory(
        Long id,
        Instant timestamp,
        String endpoint,
        String parameters,
        String response,
        Integer statusCode
) {}
