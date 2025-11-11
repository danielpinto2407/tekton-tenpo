package com.tekton.tenpo.infrastructure.adapters.in.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CalculationResponse {
    double num1;
    double num2;
    double percentage; 
    double result;
}
