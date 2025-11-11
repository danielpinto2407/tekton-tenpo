package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta que contiene el resultado del cálculo")
public record CalculationResponse(
        @Schema(description = "Primer número de entrada", example = "10.0")
        double num1,

        @Schema(description = "SEgundo número de entrada", example = "5.0")
        double num2,

        @Schema(description = "Porcentaje obtenido de la api externa", example = "8.0")
        double percentage,

        @Schema(description = "Resultado final luego del calculo", example = "16.2")
        double result
) {}
