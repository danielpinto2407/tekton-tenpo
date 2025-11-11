package com.tekton.tenpo.domain.model;

public record CalculationResult(
        double num1,
        double num2,
        double percentage,
        double result
) {

    public static CalculationResult of(double num1, double num2, double percentage) {
        double result = (num1 + num2) * (percentage / 100);
        return new CalculationResult(num1, num2, percentage, result);
    }
}
