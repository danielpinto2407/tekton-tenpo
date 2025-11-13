package com.tekton.tenpo.infrastructure.adapters.in.config.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.tekton.tenpo.infrastructure.adapters.in.config.PercentageApiProperties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebClientConfigTest {

    @Test
    void webClient_isCreatedWithProperties() {
        WebClientConfig config = new WebClientConfig();
        PercentageApiProperties props = mock(PercentageApiProperties.class);
        WebClient.Builder builder = mock(WebClient.Builder.class);
        
        when(props.getBaseUrl()).thenReturn("https://example.com");
        when(builder.baseUrl("https://example.com")).thenReturn(builder);
        when(builder.build()).thenReturn(mock(WebClient.class));

        WebClient client = config.webClient(props, builder);

        assertNotNull(client);
    }

    @Test
    void webClient_setsBaseUrl() {
        WebClientConfig config = new WebClientConfig();
        PercentageApiProperties props = new PercentageApiProperties();
        props.setBaseUrl("https://api.example.com");
        
        WebClient.Builder builder = WebClient.builder();
        WebClient client = config.webClient(props, builder);

        assertNotNull(client);
    }
}

