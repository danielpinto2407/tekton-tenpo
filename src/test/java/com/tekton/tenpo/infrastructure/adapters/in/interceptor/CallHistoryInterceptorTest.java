package com.tekton.tenpo.infrastructure.adapters.in.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.tekton.tenpo.application.port.in.SaveCallPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class CallHistoryInterceptorTest {

    @Test
    void afterCompletion_savesCallHistory_onSuccess() {
        SaveCallPort port = mock(SaveCallPort.class);
        CallHistoryInterceptor interceptor = new CallHistoryInterceptor(port);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getQueryString()).thenReturn(null);
        when(response.getStatus()).thenReturn(200);

        interceptor.afterCompletion(request, response, null, null);

        verify(port).saveCallHistory(any());
    }

    @Test
    void afterCompletion_savesCallHistory_onError() {
        SaveCallPort port = mock(SaveCallPort.class);
        CallHistoryInterceptor interceptor = new CallHistoryInterceptor(port);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Exception ex = new RuntimeException("test error");

        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getQueryString()).thenReturn(null);
        when(response.getStatus()).thenReturn(500);

        interceptor.afterCompletion(request, response, null, ex);

        verify(port).saveCallHistory(any());
    }
}
