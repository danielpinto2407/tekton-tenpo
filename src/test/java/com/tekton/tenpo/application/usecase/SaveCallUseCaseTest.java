package com.tekton.tenpo.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;

import com.tekton.tenpo.application.port.out.CallHistoryRepositoryPort;
import com.tekton.tenpo.domain.model.CallHistory;
import com.tekton.tenpo.domain.model.CreateCallHistoryInterceptor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SaveCallUseCaseTest {

    @Test
    void saveCallHistory_delegatesToRepository_andReturnsCallHistory() {
        CallHistoryRepositoryPort repo = Mockito.mock(CallHistoryRepositoryPort.class);

        SaveCallUseCase useCase = new SaveCallUseCase(repo);

        CreateCallHistoryInterceptor request = new CreateCallHistoryInterceptor("/x", "{}", "resp", 200);
        CallHistory expected = request.toEntity();

        CallHistory saved = useCase.saveCallHistory(request);

        verify(repo).save(expected);
        assertSame(CallHistory.class, saved.getClass());
    }
}
