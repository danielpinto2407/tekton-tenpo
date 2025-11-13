package com.tekton.tenpo.domain.model;

import java.time.Instant;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CrearHistorialLlamadaRequest", description = "DTO para registrar una nueva llamada en el historial")
public record CreateCallHistoryInterceptor(
        
        @Schema(description = "Endpoint que fue llamado", example = "/api/v1/users")
        String endpoint,
        
        @Schema(description = "Parámetros enviados en la llamada", example = "{\"id\":123}")
        String parameters,
        
        @Schema(description = "Respuesta obtenida de la llamada", example = "{\"success\":true}")
        String response,
        
        @Schema(description = "Código HTTP devuelto por la llamada", example = "200")
        Integer statusCode
) {
    public CallHistory toEntity() {
        return new CallHistory(
                null,
                Instant.now(),
                endpoint,
                parameters,
                response,
                statusCode
        );
    }
}
