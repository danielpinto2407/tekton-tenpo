package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for a calculation operation")
public record CalculationRequest(
        @Schema(description = "Primer numero para el cálculo", example = "10.0")
        double num1,

        @Schema(description = "Segundo numero para el cálculo", example = "5.0")
        double num2
) {}
