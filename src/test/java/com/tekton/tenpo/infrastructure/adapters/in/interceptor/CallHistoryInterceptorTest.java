package com.tekton.tenpo.infrastructure.adapters.in.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.tekton.tenpo.application.port.in.SaveCallPort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.ContentCachingRequestWrapper;

class CallHistoryInterceptorTest {

    private SaveCallPort port;
    private CallHistoryInterceptor interceptor;

    @BeforeEach
    void setUp() {
        port = mock(SaveCallPort.class);
        interceptor = new CallHistoryInterceptor(port);
    }

    @Test
    void afterCompletion_savesCallHistory_onSuccess() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/calculos");
        when(request.getQueryString()).thenReturn(null);
        when(response.getStatus()).thenReturn(200);

        interceptor.afterCompletion(request, response, null, null);

        verify(port).saveCallHistory(any());
    }

    @Test
    void afterCompletion_savesCallHistory_withExceptionMessage() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Exception ex = new RuntimeException("Service error");

        when(request.getRequestURI()).thenReturn("/api/calculos");
        when(request.getQueryString()).thenReturn(null);
        when(response.getStatus()).thenReturn(500);

        interceptor.afterCompletion(request, response, null, ex);

        verify(port).saveCallHistory(any());
    }

    @Test
    void afterCompletion_savesCallHistory_withQueryString() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/history");
        when(request.getQueryString()).thenReturn("page=0&size=10");
        when(response.getStatus()).thenReturn(200);

        interceptor.afterCompletion(request, response, null, null);

        verify(port).saveCallHistory(any());
    }

    @Test
    void afterCompletion_savesCallHistory_withBadRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getQueryString()).thenReturn(null);
        when(response.getStatus()).thenReturn(400);

        interceptor.afterCompletion(request, response, null, null);

        verify(port).saveCallHistory(any());
    }

    @Test
    void afterCompletion_handlesCachingRequestWrapper() {
        HttpServletRequest request = mock(ContentCachingRequestWrapper.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getQueryString()).thenReturn(null);
        when(response.getStatus()).thenReturn(200);
        when(((ContentCachingRequestWrapper) request).getContentAsByteArray()).thenReturn(new byte[0]);
        when(((ContentCachingRequestWrapper) request).getCharacterEncoding()).thenReturn("UTF-8");

        interceptor.afterCompletion(request, response, null, null);

        verify(port).saveCallHistory(any());
    }
}
