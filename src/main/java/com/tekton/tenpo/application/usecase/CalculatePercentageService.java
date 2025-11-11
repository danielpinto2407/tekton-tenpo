package com.tekton.tenpo.application.usecase;

import org.springframework.stereotype.Service;

import com.tekton.tenpo.application.port.in.CalculatePercentageUseCase;
import com.tekton.tenpo.application.port.out.PercentagePort;
import com.tekton.tenpo.domain.model.CalculationPercentageResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculatePercentageService implements CalculatePercentageUseCase {

    private final PercentagePort percentagePort;

    @Override
    public CalculationPercentageResult calculate(double num1, double num2) {
        double percentage = percentagePort.getPercentage();
        return CalculationPercentageResult.of(num1, num2, percentage);
    }
}
