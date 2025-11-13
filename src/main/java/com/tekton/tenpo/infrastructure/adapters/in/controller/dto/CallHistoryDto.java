package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import java.time.Instant;

import com.tekton.tenpo.domain.model.CallHistory;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CallHistory", description = "Registro de una llamada en el historial")
public record CallHistoryDto(
        @Schema(description = "Identificador único", example = "1") Long id,
        @Schema(description = "Marca de tiempo (UTC)", example = "2025-11-13T12:00:00Z") Instant timestamp,
        @Schema(description = "Endpoint que fue llamado", example = "/api/v1/users") String endpoint,
        @Schema(description = "Parámetros enviados en la llamada", example = "{\"id\":123}") String parameters,
        @Schema(description = "Respuesta obtenida de la llamada", example = "{\"success\":true}") String response,
        @Schema(description = "Código HTTP devuelto por la llamada", example = "200") Integer statusCode
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

