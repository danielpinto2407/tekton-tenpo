package com.tekton.tenpo.infrastructure.adapters.in.controller.mapper;

import com.tekton.tenpo.domain.model.CalculationResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;

import org.springframework.stereotype.Component;

@Component
public class CalculationResponseMapper {

    public CalculationResponse toResponse(CalculationResult result) {
        return new CalculationResponse(
                result.num1(),
                result.num2(),
                result.percentage(),
                result.result()
        );
    }
}

