package com.tekton.tenpo.infrastructure.adapters.in.controller.mapper;

import com.tekton.tenpo.domain.model.CalculationResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;

import org.springframework.stereotype.Component;

@Component
public class CalculationResponseMapper {

    public CalculationResponse toResponse(CalculationResult result) {
        return CalculationResponse.builder()
                .num1(result.getNum1())
                .num2(result.getNum2())
                .percentage(result.getPercentage())
                .result(result.getResult())
                .build();
    }
}
