package com.tekton.tenpo.infrastructure.adapters.in.config.webclient;   

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.tekton.tenpo.infrastructure.adapters.in.config.PercentageApiProperties;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(PercentageApiProperties properties, WebClient.Builder builder) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .build();
    }
}

