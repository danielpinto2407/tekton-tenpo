package com.tekton.tenpo.infrastructure.adapters.in.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tekton.tenpo.application.port.in.CalculatePercentagePort;
import com.tekton.tenpo.domain.model.CalculationPercentageResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationRequest;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;
import com.tekton.tenpo.infrastructure.adapters.out.persistence.mapper.CalculationResponseMapper;
import com.tekton.tenpo.infrastructure.constants.ApiConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiConstants.CALCULOS_API_PATH)
@RequiredArgsConstructor
@Tag(name = "Calculos", description = "Operaciones para calculo de porcentaje")
public class CalculationController {

    private final CalculatePercentagePort calculateUseCase;
    private final CalculationResponseMapper responseMapper;

    @Operation(
        summary = "Calcula la suma de dos números con porcentaje dinámico",
        description = "Suma dos números positivos y aplica un porcentaje obtenido de CSRNG.net. "
            + "Usa Circuit Breaker con fallback a caché si el servicio externo falla. "
            + "Registra automáticamente la llamada en el historial de forma asíncrona.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cálculo exitoso: (num1 + num2) + ((num1 + num2) * percentage / 100)",
                content = @Content(schema = @Schema(implementation = CalculationResponse.class))
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Validación fallida: números deben ser positivos",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "502",
                description = "Servicio externo no disponible: usa fallback de caché",
                content = @Content
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Error inesperado en la aplicación",
                content = @Content
            )
        }
    )
    @PostMapping
    public ResponseEntity<CalculationResponse> calculate(@RequestBody @Valid CalculationRequest request) {
        CalculationPercentageResult result = calculateUseCase.calculate(request.num1(), request.num2());
        return ResponseEntity.ok(responseMapper.toResponse(result));
    }
}


