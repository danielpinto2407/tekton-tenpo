package com.tekton.tenpo.domain.model;

public record CalculationPercentageResult(
        double num1,
        double num2,
        double percentage,
        double result
) {

    public static CalculationPercentageResult of(double num1, double num2, double percentage) {
        double result = (num1 + num2) * (percentage / 100);
        return new CalculationPercentageResult(num1, num2, percentage, result);
    }
}
