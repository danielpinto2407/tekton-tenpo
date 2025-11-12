package com.tekton.tenpo.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CalculationPercentageResultTest {

    private double num1;
    private double num2;
    private double percentage;

    @BeforeEach
    void setUp() {
        num1 = 10.0;
        num2 = 20.0;
        percentage = 10.0;
    }

    @Test
    void shouldCalculateResultCorrectly_givenPositiveNumbers() {
        CalculationPercentageResult result = CalculationPercentageResult.of(num1, num2, percentage);
        double expectedSum = num1 + num2;
        double expectedResult = expectedSum + (expectedSum * (percentage / 100));
        assertThat(result.num1()).isEqualTo(num1);
        assertThat(result.num2()).isEqualTo(num2);
        assertThat(result.percentage()).isEqualTo(percentage);
        assertThat(result.result()).isEqualTo(expectedResult);
    }

    @Test
    void shouldHandleZeroValues() {
        num1 = 0.0;
        num2 = 0.0;
        percentage = 50.0;
        CalculationPercentageResult result = CalculationPercentageResult.of(num1, num2, percentage);
        assertThat(result.result()).isZero();
    }

    @Test
    void shouldHandleNegativeNumbers() {
        num1 = -5.0;
        num2 = -15.0;
        percentage = 20.0;
        CalculationPercentageResult result = CalculationPercentageResult.of(num1, num2, percentage);
        double expectedSum = num1 + num2;
        double expectedResult = expectedSum + (expectedSum * (percentage / 100));
        assertThat(result.result()).isEqualTo(expectedResult);
    }

    @Test
    void shouldReturnSameValuesAsProvided() {
        num1 = 7.5;
        num2 = 2.5;
        percentage = 0.0;
        CalculationPercentageResult result = CalculationPercentageResult.of(num1, num2, percentage);
        assertThat(result.num1()).isEqualTo(7.5);
        assertThat(result.num2()).isEqualTo(2.5);
        assertThat(result.percentage()).isZero();
        assertThat(result.result()).isEqualTo(10.0);
    }
}
