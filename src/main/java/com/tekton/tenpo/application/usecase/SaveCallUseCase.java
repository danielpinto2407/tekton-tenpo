package com.tekton.tenpo.application.usecase;

import org.springframework.stereotype.Service;

import com.tekton.tenpo.application.port.in.SaveCallPort;
import com.tekton.tenpo.application.port.out.CallHistoryRepositoryPort;
import com.tekton.tenpo.domain.model.CallHistory;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CreateCallHistoryRequest;

import lombok.RequiredArgsConstructor;

/**
 * Implementación del caso de uso para guardar registros de historial.
 */
@Service
@RequiredArgsConstructor
public class SaveCallUseCase implements SaveCallPort {

    private final CallHistoryRepositoryPort repository;

    public CallHistory saveCallHistory(CreateCallHistoryRequest request) {
        return save(request.toEntity());
    }

    public CallHistory save(CallHistory callHistory) {
        repository.save(callHistory);
        return callHistory;
    }
}

