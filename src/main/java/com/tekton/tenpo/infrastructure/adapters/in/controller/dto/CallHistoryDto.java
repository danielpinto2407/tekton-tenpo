package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import java.time.Instant;

import com.tekton.tenpo.domain.model.CallHistory;

public record CallHistoryDto(
        Long id,
        Instant timestamp,
        String endpoint,
        String parameters,
        String response,
        Integer statusCode
) {
    public static CallHistoryDto fromEntity(CallHistory entity) {
        return new CallHistoryDto(
                entity.id(),
                entity.timestamp(),
                entity.endpoint(),
                entity.parameters(),
                entity.response(),
                entity.statusCode()
        );
    }
}

