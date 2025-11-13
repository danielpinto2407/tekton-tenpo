package com.tekton.tenpo.infrastructure.adapters.out.persistence.mapper;

import com.tekton.tenpo.domain.model.CalculationPercentageResult;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CalculationResponseMapperTest {

    private final CalculationResponseMapper mapper = Mappers.getMapper(CalculationResponseMapper.class);

    @Test
    void toResponse_mapsFields() {
        CalculationPercentageResult result = new CalculationPercentageResult(5.0, 10.0, 15.0, 33.3);

        CalculationResponse response = mapper.toResponse(result);

        assertNotNull(response);
        assertEquals(5.0, response.num1());
        assertEquals(10.0, response.num2());
        assertEquals(15.0, response.percentage());
        assertEquals(33.3, response.result());
    }
}
