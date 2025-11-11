package com.tekton.tenpo.infrastructure.adapters.out.external.dto;

public record PercentageApiResponse(
        String status,
        int min,
        int max,
        double random
) {}
