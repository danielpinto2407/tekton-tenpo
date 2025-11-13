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
        summary = "Calcula la suma de los dos numeros y les aplica el porcentaje obtenido de la api externa",
        description = "Recibe dos números, los suma y les aplica un porcentaje obtenido de un servicio externo.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Calculo completado exitosamente",
                content = @Content(schema = @Schema(implementation = CalculationResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<CalculationResponse> calculate(@RequestBody @Valid CalculationRequest request) {
        CalculationPercentageResult result = calculateUseCase.calculate(request.num1(), request.num2());
        return ResponseEntity.ok(responseMapper.toResponse(result));
    }
}


