package com.tekton.tenpo.application.usecase;

import org.springframework.stereotype.Service;

import com.tekton.tenpo.application.port.in.CalculatePercentagePort;
import com.tekton.tenpo.application.port.out.ExternalPercentagePort;
import com.tekton.tenpo.domain.model.CalculationPercentageResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculatePercentageUseCase implements CalculatePercentagePort {

    private final ExternalPercentagePort percentagePort;

    @Override
    public CalculationPercentageResult calculate(double num1, double num2) {
        double percentage = percentagePort.getPercentage();
        return CalculationPercentageResult.of(num1, num2, percentage);
    }
}
