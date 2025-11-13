package com.tekton.tenpo.infrastructure.adapters.out.external;

import com.tekton.tenpo.infrastructure.adapters.in.config.PercentageApiProperties;
import com.tekton.tenpo.infrastructure.adapters.in.controller.exception.ExternalServiceException;
import com.tekton.tenpo.infrastructure.adapters.out.external.dto.RandomResponse;
import com.tekton.tenpo.infrastructure.constants.ApiConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PercentageApiAdapterTest {

  /*   @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private PercentageApiProperties properties;

    private PercentageApiAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(properties.getBaseUrl()).thenReturn("http://localhost:8080");
        when(properties.getPath()).thenReturn("/api/random");

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        adapter = new PercentageApiAdapter(webClientBuilder, properties);
    }

    // --- Helper para generar respuesta aleatoria entre 1 y 100 ---
    private RandomResponse randomResponse() {
        int value = new Random().nextInt(100) + 1;
        return new RandomResponse((double) value);
    }

    // --- Tests ---

    @Test
    void getPercentage_shouldReturnRandomValueAndCacheIt() {
        RandomResponse response = randomResponse();
        when(responseSpec.bodyToFlux(RandomResponse.class))
                .thenReturn(Flux.just(response));

        double result = adapter.getPercentage();

        assertTrue(result >= 1 && result <= 100);

        // Fallback debe devolver el mismo valor cacheado
        double fallback = invokeFallback(new RuntimeException("Simulated error"));
        assertEquals(result, fallback, 0.001);
    }

    @Test
    void getPercentage_shouldThrow_whenResponseIsNull() {
        when(responseSpec.bodyToFlux(RandomResponse.class)).thenReturn(Flux.just((RandomResponse) null));

        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> adapter.getPercentage());
        assertTrue(ex.getMessage().contains(ApiConstants.EXTERNAL_INVALID_RESPONSE_EXCEPTION));
    }

    @Test
    void getPercentage_shouldThrow_whenRandomFieldIsNull() {
        RandomResponse response = new RandomResponse(null);
        when(responseSpec.bodyToFlux(RandomResponse.class)).thenReturn(Flux.just(response));

        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> adapter.getPercentage());
        assertTrue(ex.getMessage().contains(ApiConstants.EXTERNAL_INVALID_RESPONSE_EXCEPTION));
    }

    @Test
    void getPercentage_shouldThrow_whenFluxEmpty() {
        when(responseSpec.bodyToFlux(RandomResponse.class)).thenReturn(Flux.empty());

        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> adapter.getPercentage());
        assertTrue(ex.getMessage().contains(ApiConstants.EXTERNAL_SERVICE_EXCEPTION));
    }

    @Test
    void getPercentage_shouldThrow_whenFluxErrors() {
        when(responseSpec.bodyToFlux(RandomResponse.class))
                .thenReturn(Flux.error(new RuntimeException("Simulated connection error")));

        ExternalServiceException ex = assertThrows(ExternalServiceException.class, () -> adapter.getPercentage());
        assertTrue(ex.getMessage().contains(ApiConstants.EXTERNAL_SERVICE_EXCEPTION));
    }

    @Test
    void fallback_shouldReturnCachedValue_whenCachePresent() {
        RandomResponse response = randomResponse();
        when(responseSpec.bodyToFlux(RandomResponse.class)).thenReturn(Flux.just(response));

        double first = adapter.getPercentage();
        double fallback = invokeFallback(new RuntimeException("Simulated error"));

        assertEquals(first, fallback, 0.001);
    }

    @Test
    void fallback_shouldThrow_whenCacheEmpty() {
        ExternalServiceException ex = assertThrows(
                ExternalServiceException.class,
                () -> invokeFallback(new RuntimeException("No cache"))
        );

        assertTrue(ex.getMessage().contains(ApiConstants.FALLBACK_MESSAGE));
    }

    // --- Helper para invocar fallback privado ---
    private double invokeFallback(Throwable t) {
        try {
            var method = PercentageApiAdapter.class.getDeclaredMethod("fallbackPercentage", Throwable.class);
            method.setAccessible(true);
            return (double) method.invoke(adapter, t);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } */
}
