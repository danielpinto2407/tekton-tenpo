package com.tekton.tenpo.infrastructure.adapters.out.persistence.mapper;

import org.mapstruct.Mapper;
import com.tekton.tenpo.domain.model.CalculationPercentageResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;

@Mapper(componentModel = "spring")
public interface CalculationResponseMapper {

    CalculationResponse toResponse(CalculationPercentageResult result);
}
