package com.tekton.tenpo.application.port.in;

import com.tekton.tenpo.domain.model.CallHistory;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CreateCallHistoryRequest;

public interface SaveCallPort {
    CallHistory saveCallHistory(CreateCallHistoryRequest request);
}
