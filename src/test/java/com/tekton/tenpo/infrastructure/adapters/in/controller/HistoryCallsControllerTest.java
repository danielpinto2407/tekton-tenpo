package com.tekton.tenpo.infrastructure.adapters.in.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tekton.tenpo.application.port.in.GetAllHistoryCallsPort;
import com.tekton.tenpo.domain.model.CallHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

class HistoryCallsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GetAllHistoryCallsPort port;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        HistoryCallsController controller = new HistoryCallsController(port);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllHistory_returnsPageOfHistory() throws Exception {
        CallHistory history = new CallHistory(
                1L,
                Instant.now(),
                "/api/test",
                "{}",
                "ok",
                200
        );

        Page<CallHistory> page = new PageImpl<>(List.of(history), PageRequest.of(0, 10), 1);
        when(port.getHistory(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/history?page=0&size=10"))
                .andExpect(status().isOk());
    }
}
