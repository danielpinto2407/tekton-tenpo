package com.tekton.tenpo.application.port.in;

import com.tekton.tenpo.domain.model.CalculationResult;

public interface CalculateUseCase {
    CalculationResult calculate(double num1, double num2);
}
