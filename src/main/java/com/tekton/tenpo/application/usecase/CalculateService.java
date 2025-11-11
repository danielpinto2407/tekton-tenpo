package com.tekton.tenpo.application.usecase;

import org.springframework.stereotype.Service;

import com.tekton.tenpo.application.port.in.CalculateUseCase;
import com.tekton.tenpo.application.port.out.PercentagePort;
import com.tekton.tenpo.domain.model.CalculationResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculateService implements CalculateUseCase {

    private final PercentagePort percentagePort;

    @Override
    public CalculationResult calculate(double num1, double num2) {
        double percentage = percentagePort.getPercentage();
        return CalculationResult.of(num1, num2, percentage);
    }
}
