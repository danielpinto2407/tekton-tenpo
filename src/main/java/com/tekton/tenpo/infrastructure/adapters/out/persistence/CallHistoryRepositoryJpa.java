package com.tekton.tenpo.infrastructure.adapters.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.tekton.tenpo.application.port.out.CallHistoryRepositoryPort;
import com.tekton.tenpo.domain.model.CallHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.scheduling.annotation.Async;

@Component
@RequiredArgsConstructor
@Log4j2
public class CallHistoryRepositoryJpa implements CallHistoryRepositoryPort {

    private final SpringDataCallHistoryRepository repo;

    @Async("historyExecutor")
    @Override
    public void save(CallHistory callHistory) {
        log.info("[Async Save] Guardando historial en hilo: {}", Thread.currentThread().getName());
        repo.save(callHistory);
    }

    @Override
    public Page<CallHistory> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
