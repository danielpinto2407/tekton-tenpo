package com.tekton.tenpo.infrastructure.constants;

public final class ApiConstants {

    private ApiConstants() {}
    
    public static final String FALLBACK_MESSAGE = "Servicio externo no disponible y no hay porcentaje en caché.";
    public static final String EXTERNAL_SERVICE_EXCEPTION = "No se pudo obtener el porcentaje externo.";
    public static final String ERROR_FETCHING_PERCENTAGE = "Error fetching percentage: {}";
    public static final String EXTERNAL_INVALID_RESPONSE_EXCEPTION = "Respuesta inválida del servicio externo.";
    public static final String CACHE_KEY = "lastPercentage";
    public static final String CIRCUIT_BREAKER_NAME = "percentageApi";
    public static final String FALLBACK_METHOD = "fallbackPercentage";

    // Controllers
    public static final String CALCULOS_API_PATH = "/api/v1/calculos";
    public static final String EMPTY_BODY_RESPONSE = "{}";
    public static final String SUCCESS = "success";
}
