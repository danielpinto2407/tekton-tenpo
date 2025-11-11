package com.tekton.tenpo.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalculationResult {
    private final double num1;
    private final double num2;
    private final double percentage;
    private final double result;

    public static CalculationResult of(double num1, double num2, double percentage) {
        double result = (num1 + num2) * percentage/100;
        return CalculationResult.builder()
                .num1(num1)
                .num2(num2)
                .percentage(percentage)
                .result(result)
                .build();
    }
}

//CalculationResult result = CalculationResult.of(10, 5, 0.1);


