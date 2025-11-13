package com.tekton.tenpo.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekton.tenpo.domain.model.CallHistory;

public interface GetAllHistoryCallsPort {

    Page<CallHistory> getHistory(Pageable pageable);
}
