package com.tekton.tenpo.application.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tekton.tenpo.application.port.out.CallHistoryRepositoryPort;
import com.tekton.tenpo.domain.model.CallHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class SaveCallUseCaseTest {

    @Mock
    private CallHistoryRepositoryPort callHistoryRepositoryPort;

    @InjectMocks
    private SaveCallUseCase saveCallUseCase;

    @Test
    void save_delegatesToRepository_andReturnsCallHistory() {
        // Arrange
        var now = Instant.now();
        var input = new CallHistory(
                null,
                now,
                "/api/calculate",
                "{\"a\":1,\"b\":2}",
                "{\"result\":3}",
                200
        );

        doNothing().when(callHistoryRepositoryPort).save(any());

        // Act
        var result = saveCallUseCase.save(input);

        // Assert
        assertNotNull(result);
        assertEquals("/api/calculate", result.endpoint());
        assertEquals("{\"a\":1,\"b\":2}", result.parameters());
        assertEquals("{\"result\":3}", result.response());
        assertEquals(200, result.statusCode());
        assertEquals(now, result.timestamp());

        ArgumentCaptor<CallHistory> captor = ArgumentCaptor.forClass(CallHistory.class);
        verify(callHistoryRepositoryPort).save(captor.capture());
        
        var captured = captor.getValue();
        assertEquals(input.endpoint(), captured.endpoint());
        assertEquals(input.parameters(), captured.parameters());
        assertEquals(input.response(), captured.response());
        assertEquals(input.statusCode(), captured.statusCode());
    }
}
