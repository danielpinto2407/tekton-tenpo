package com.tekton.tenpo.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekton.tenpo.domain.model.CallHistory;

public interface CallHistoryRepositoryPort {
    void save(CallHistory callHistory);
    Page<CallHistory> findAll(Pageable pageable);
}

