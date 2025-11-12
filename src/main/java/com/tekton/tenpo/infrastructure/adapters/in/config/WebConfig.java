package com.tekton.tenpo.infrastructure.adapters.in.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tekton.tenpo.infrastructure.adapters.in.interceptor.CallHistoryInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CallHistoryInterceptor callHistoryInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(callHistoryInterceptor)
                .addPathPatterns("/api/v1/calculos") // solo esa ruta exacta
                .excludePathPatterns(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/actuator/**",
                    "/error"
                );
    }
}

