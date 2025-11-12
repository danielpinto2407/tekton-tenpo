package com.tekton.tenpo.infrastructure.adapters.out.external;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tekton.tenpo.application.port.out.ExternalPercentagePort;
import com.tekton.tenpo.infrastructure.adapters.in.controller.exception.ExternalServiceException;
import com.tekton.tenpo.infrastructure.adapters.out.external.dto.RandomResponse;
import com.tekton.tenpo.infrastructure.config.PercentageApiProperties;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Optional;

/**
 * Adapter encargado de consumir un servicio externo para obtener un porcentaje aleatorio.
 * Implementa caché local (Caffeine) y tolerancia a fallos con CircuitBreaker (Resilience4j).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PercentageApiAdapter implements ExternalPercentagePort {

    private static final String CACHE_KEY = "lastPercentage";

    private final WebClient.Builder webClientBuilder;
    private final PercentageApiProperties properties;

    private final Cache<String, Double> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))
            .maximumSize(1)
            .build();
            
    @Override
    @CircuitBreaker(name = "percentageApi", fallbackMethod = "fallbackPercentage")
    public double getPercentage() {
        log.info("Fetching percentage from external API...");

        WebClient webClient = webClientBuilder.baseUrl(properties.getBaseUrl()).build();

        return webClient.get()
                .uri(properties.getPath())
                .retrieve()
                .bodyToFlux(RandomResponse.class)
                .next()
                .map(response -> {
                    if (response == null || response.random() == null) {
                        log.error("Invalid response from external API: {}", response);
                        throw new ExternalServiceException("Respuesta inválida del servicio externo.");
                    }
                    double percentage = response.random();
                    cache.put(CACHE_KEY, percentage);
                    log.info("Fetched and cached new percentage: {}", percentage);
                    return percentage;
                })
                .doOnError(e -> log.error("Error fetching percentage: {}", e.getMessage()))
                .blockOptional(Duration.ofSeconds(3))
                .orElseThrow(() -> new ExternalServiceException("No se pudo obtener el porcentaje externo."));
    }

    /**
     * Fallback activado por Resilience4j si ocurre una falla o timeout.
     * Usa el valor en caché si existe.
     */
    private double fallbackPercentage(Throwable ex) {
        log.warn("Fallback triggered due to error: {}", ex.getMessage());
        return Optional.ofNullable(cache.getIfPresent(CACHE_KEY))
                .orElseThrow(() -> new ExternalServiceException("Servicio externo no disponible y no hay porcentaje en caché."));
    }
    
}
