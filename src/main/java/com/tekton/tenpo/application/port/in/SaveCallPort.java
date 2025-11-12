package com.tekton.tenpo.application.port.in;

import com.tekton.tenpo.domain.model.CallHistory;
import com.tekton.tenpo.domain.model.CreateCallHistoryInterceptor;

public interface SaveCallPort {
    CallHistory saveCallHistory(CreateCallHistoryInterceptor request);
}
