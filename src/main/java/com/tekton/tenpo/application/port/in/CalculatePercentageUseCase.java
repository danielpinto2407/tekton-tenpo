package com.tekton.tenpo.application.port.in;

import com.tekton.tenpo.domain.model.CalculationPercentageResult;

public interface CalculatePercentageUseCase {
    CalculationPercentageResult calculate(double num1, double num2);
}
