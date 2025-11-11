package com.tekton.tenpo.infrastructure.adapters.in.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tekton.tenpo.application.port.in.CalculateUseCase;
import com.tekton.tenpo.domain.model.CalculationResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationRequest;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;
import com.tekton.tenpo.infrastructure.adapters.in.controller.mapper.CalculationResponseMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/calculations")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculateUseCase calculateUseCase;
    private final CalculationResponseMapper responseMapper;

    @PostMapping
    public ResponseEntity<CalculationResponse> calculate(@RequestBody CalculationRequest request) {
        CalculationResult result = calculateUseCase.calculate(request.getNum1(), request.getNum2());
        return ResponseEntity.ok(responseMapper.toResponse(result));
    }
}


