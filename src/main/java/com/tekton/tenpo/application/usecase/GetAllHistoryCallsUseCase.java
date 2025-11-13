package com.tekton.tenpo.application.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tekton.tenpo.application.port.in.GetAllHistoryCallsPort;
import com.tekton.tenpo.application.port.out.CallHistoryRepositoryPort;
import com.tekton.tenpo.domain.model.CallHistory;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del caso de uso para obtener el historial de llamadas.
 */
@Service
@RequiredArgsConstructor
public class GetAllHistoryCallsUseCase implements GetAllHistoryCallsPort {

    private final CallHistoryRepositoryPort repository;

    @Override
    public Page<CallHistory> getHistory(Pageable pageable) {
        return repository.findAll(pageable);
    }
}