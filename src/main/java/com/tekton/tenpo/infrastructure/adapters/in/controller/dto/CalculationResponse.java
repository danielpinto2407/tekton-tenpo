package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CalculationResponse", description = "Response con el resultado de la suma más el porcentaje aplicado")
public record CalculationResponse(
        @Schema(description = "Primer número de entrada", example = "100.0")
        double num1,

        @Schema(description = "Segundo número de entrada", example = "50.0")
        double num2,

        @Schema(description = "Porcentaje obtenido del servicio externo (CSRNG.net)", example = "15.5")
        double percentage,

        @Schema(description = "Resultado final: (num1 + num2) + ((num1 + num2) * percentage / 100)", example = "177.75")
        double result
) {}
