package com.tekton.tenpo.infrastructure.adapters.out.external;

import com.tekton.tenpo.infrastructure.adapters.in.config.PercentageApiProperties;
import com.tekton.tenpo.infrastructure.adapters.in.controller.exception.ExternalServiceException;
import com.tekton.tenpo.infrastructure.adapters.out.external.dto.RandomResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class PercentageApiAdapterTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private PercentageApiProperties properties;

    private PercentageApiAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doReturn("https://example.com").when(properties).getBaseUrl();
        doReturn("/api/random").when(properties).getPath();
        adapter = new PercentageApiAdapter(webClientBuilder, properties);
    }

    @Test
    void getPercentage_returnsValidPercentage_whenJsonResponseIsValid() {
        RandomResponse response = new RandomResponse("success", 0, 100, 42.5);
        WebClient webClient = mock(WebClient.class);
        
        doReturn(webClientBuilder).when(webClientBuilder).baseUrl(anyString());
        doReturn(webClient).when(webClientBuilder).build();
        
        WebClient.RequestHeadersUriSpec<?> uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        doReturn(uriSpec).when(webClient).get();
        
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        doReturn(headersSpec).when(uriSpec).uri(anyString());
        
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(responseSpec).when(headersSpec).retrieve();
        
        Flux<RandomResponse> flux = Flux.just(response);
        doReturn(flux).when(responseSpec).bodyToFlux(RandomResponse.class);

        double result = adapter.getPercentage();

        assertEquals(42.5, result);
    }

    @Test
    void getPercentage_throwsExternalServiceException_whenResponseIsEmpty() {
        WebClient webClient = mock(WebClient.class);
        
        doReturn(webClientBuilder).when(webClientBuilder).baseUrl(anyString());
        doReturn(webClient).when(webClientBuilder).build();
        
        WebClient.RequestHeadersUriSpec<?> uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        doReturn(uriSpec).when(webClient).get();
        
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        doReturn(headersSpec).when(uriSpec).uri(anyString());
        
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(responseSpec).when(headersSpec).retrieve();
        
        Flux<RandomResponse> flux = Flux.empty();
        doReturn(flux).when(responseSpec).bodyToFlux(RandomResponse.class);

        assertThrows(ExternalServiceException.class, () -> adapter.getPercentage());
    }

    @Test
    void getPercentage_throwsExternalServiceException_whenResponseHasNullRandomValue() {
        RandomResponse responseWithNull = new RandomResponse("success", 0, 100, null);
        WebClient webClient = mock(WebClient.class);
        
        doReturn(webClientBuilder).when(webClientBuilder).baseUrl(anyString());
        doReturn(webClient).when(webClientBuilder).build();
        
        WebClient.RequestHeadersUriSpec<?> uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        doReturn(uriSpec).when(webClient).get();
        
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        doReturn(headersSpec).when(uriSpec).uri(anyString());
        
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(responseSpec).when(headersSpec).retrieve();
        
        Flux<RandomResponse> flux = Flux.just(responseWithNull);
        doReturn(flux).when(responseSpec).bodyToFlux(RandomResponse.class);

        assertThrows(ExternalServiceException.class, () -> adapter.getPercentage());
    }

    @Test
    void getPercentage_returnsValidPercentage_withZeroValue() {
        RandomResponse response = new RandomResponse("success", 0, 100, 0.0);
        WebClient webClient = mock(WebClient.class);
        
        doReturn(webClientBuilder).when(webClientBuilder).baseUrl(anyString());
        doReturn(webClient).when(webClientBuilder).build();
        
        WebClient.RequestHeadersUriSpec<?> uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        doReturn(uriSpec).when(webClient).get();
        
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        doReturn(headersSpec).when(uriSpec).uri(anyString());
        
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(responseSpec).when(headersSpec).retrieve();
        
        Flux<RandomResponse> flux = Flux.just(response);
        doReturn(flux).when(responseSpec).bodyToFlux(RandomResponse.class);

        double result = adapter.getPercentage();

        assertEquals(0.0, result);
    }
}
