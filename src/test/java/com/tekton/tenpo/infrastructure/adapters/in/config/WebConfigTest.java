package com.tekton.tenpo.infrastructure.adapters.in.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.tekton.tenpo.infrastructure.adapters.in.interceptor.CallHistoryInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

class WebConfigTest {

    @Test
    void addInterceptors_registersCallHistoryInterceptor() {
        CallHistoryInterceptor mockInterceptor = mock(CallHistoryInterceptor.class);
        WebConfig config = new WebConfig(mockInterceptor);
        
        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        var registrationMock = mock(org.springframework.web.servlet.config.annotation.InterceptorRegistration.class);
        
        org.mockito.Mockito.when(registry.addInterceptor(any())).thenReturn(registrationMock);
        org.mockito.Mockito.when(registrationMock.addPathPatterns(anyString())).thenReturn(registrationMock);
        org.mockito.Mockito.when(registrationMock.excludePathPatterns(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(registrationMock);

        config.addInterceptors(registry);

        verify(registry).addInterceptor(mockInterceptor);
    }

    @Test
    void webConfig_constructsWithInterceptor() {
        CallHistoryInterceptor mockInterceptor = mock(CallHistoryInterceptor.class);
        WebConfig config = new WebConfig(mockInterceptor);

        assert config != null;
    }
}
