package com.tekton.tenpo.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.tekton.tenpo.application.port.out.ExternalPercentagePort;
import com.tekton.tenpo.domain.model.CalculationPercentageResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CalculatePercentageUseCaseTest {

    @Test
    void calculate_returnsExpectedResult_givenExternalPercentage() {
        ExternalPercentagePort percentagePort = Mockito.mock(ExternalPercentagePort.class);
        when(percentagePort.getPercentage()).thenReturn(50.0);

        CalculatePercentageUseCase useCase = new CalculatePercentageUseCase(percentagePort);

        CalculationPercentageResult result = useCase.calculate(100.0, 50.0);

        assertEquals(50.0, result.percentage());
        assertEquals(225.0, result.result());
    }
}
