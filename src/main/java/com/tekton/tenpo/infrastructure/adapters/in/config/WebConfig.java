package com.tekton.tenpo.infrastructure.adapters.in.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tekton.tenpo.infrastructure.adapters.in.interceptor.CallHistoryInterceptor;
import com.tekton.tenpo.infrastructure.constants.ApiConstants;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CallHistoryInterceptor callHistoryInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(callHistoryInterceptor)
                .addPathPatterns(ApiConstants.CALCULOS_API_PATH)
                .excludePathPatterns(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/**",
                    "/error"
                );
    }
}

