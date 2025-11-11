package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import com.tekton.tenpo.infrastructure.constants.ControllerMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request for a calculation operation")
public record CalculationRequest(
        @NotNull(message = ControllerMessages.VALIDATION_ERROR)
        @Positive(message = ControllerMessages.POSITIVE_ERROR)
        @Schema(description = "Primer numero para el cálculo", example = "10.0")
        double num1,

        @NotNull(message = ControllerMessages.VALIDATION_ERROR)
        @Positive(message = ControllerMessages.POSITIVE_ERROR)
        @Schema(description = "Segundo numero para el cálculo", example = "5.0")
        double num2
) {}
