package com.tekton.tenpo.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.tekton.tenpo.application.port.out.CallHistoryRepositoryPort;
import com.tekton.tenpo.domain.model.CallHistory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

class GetAllHistoryCallsUseCaseTest {

    @Test
    void getHistory_returnsPageFromRepository() {
        CallHistoryRepositoryPort repo = Mockito.mock(CallHistoryRepositoryPort.class);
        GetAllHistoryCallsUseCase useCase = new GetAllHistoryCallsUseCase(repo);

        Pageable pageable = PageRequest.of(0, 10);
        CallHistory history = new CallHistory(
                1L,
                Instant.now(),
                "/api/test",
                "{}",
                "ok",
                200
        );
        Page<CallHistory> expected = new PageImpl<>(List.of(history), pageable, 1);

        when(repo.findAll(pageable)).thenReturn(expected);

        Page<CallHistory> result = useCase.getHistory(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("/api/test", result.getContent().get(0).endpoint());
    }
}
