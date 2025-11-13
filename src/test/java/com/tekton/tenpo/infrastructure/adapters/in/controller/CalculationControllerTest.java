package com.tekton.tenpo.infrastructure.adapters.in.controller;

import com.tekton.tenpo.application.port.in.CalculatePercentagePort;
import com.tekton.tenpo.domain.model.CalculationPercentageResult;
import com.tekton.tenpo.infrastructure.adapters.out.persistence.mapper.CalculationResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CalculationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalculatePercentagePort calculateUseCase;

    @Mock
    private CalculationResponseMapper responseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CalculationController controller = new CalculationController(calculateUseCase, responseMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void calculate_returnsOkWithResult() throws Exception {
        CalculationPercentageResult result =
                new CalculationPercentageResult(10.0, 32.0, 20.0, 84.4);

        when(calculateUseCase.calculate(anyDouble(), anyDouble()))
                .thenReturn(result);
        when(responseMapper.toResponse(result))
                .thenReturn(new com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CalculationResponse(10.0, 32.0, 20.0, 84.4));

        mockMvc.perform(post("/api/v1/calculos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"num1\":10.0,\"num2\":32.0}"))
                .andExpect(status().isOk());
    }
}
