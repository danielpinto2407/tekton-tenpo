package com.tekton.tenpo.infrastructure.adapters.in.config.async;

import org.junit.jupiter.api.Test;

import com.tekton.tenpo.infrastructure.constants.AdapterConstants;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncConfigTest {

    @Test
    void historyExecutor_isCreated() {
        AsyncConfig config = new AsyncConfig();
        Executor executor = config.historyExecutor();

        assertNotNull(executor);
    }

    @Test
    void historyExecutor_isThreadPoolTaskExecutor() {
        AsyncConfig config = new AsyncConfig();
        Executor executor = config.historyExecutor();

        assertTrue(executor instanceof org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor);
    }

    @Test
    void historyExecutor_hasCorrectConfiguration() {
        AsyncConfig config = new AsyncConfig();
        org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor executor = 
            (org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor) config.historyExecutor();

        assert executor.getCorePoolSize() == 2;
        assert executor.getMaxPoolSize() == 4;
    }
}
