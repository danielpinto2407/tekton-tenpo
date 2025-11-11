package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalculationRequest {
    private double num1;
    private double num2;
}
