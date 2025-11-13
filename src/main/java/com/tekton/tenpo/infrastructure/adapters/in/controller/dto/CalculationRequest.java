package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import com.tekton.tenpo.infrastructure.constants.ControllerMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "CalculationRequest", description = "Request para calcular la suma de dos números con porcentaje dinámico")
public record CalculationRequest(
        @NotNull(message = ControllerMessages.VALIDATION_ERROR)
        @Positive(message = ControllerMessages.POSITIVE_ERROR)
        @Schema(description = "Primer número a sumar", example = "100.0", minimum = "0.01")
        double num1,

        @NotNull(message = ControllerMessages.VALIDATION_ERROR)
        @Positive(message = ControllerMessages.POSITIVE_ERROR)
        @Schema(description = "Segundo número a sumar", example = "50.0", minimum = "0.01")
        double num2
) {}
