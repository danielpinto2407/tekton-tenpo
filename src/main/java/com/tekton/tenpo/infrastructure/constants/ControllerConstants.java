package com.tekton.tenpo.infrastructure.constants;

public final class ControllerConstants {

    private ControllerConstants() {}

    public static final String CALCULOS_TAG_NAME = "Calculos";
    public static final String CALCULOS_TAG_DESCRIPTION = "Operaciones para calculo de porcentaje";

    public static final String CALCULATION_OPERATION_SUMMARY = "Calcula la suma de los dos numeros y les aplica el porcentaje obtenido de la api externa";
    public static final String CALCULATION_OPERATION_DESCRIPTION = "Recibe dos números, los suma y les aplica un porcentaje obtenido de un servicio externo.";

    public static final String API_RESPONSE_200_CALCULO = "Calculo completado exitosamente";
    public static final String API_RESPONSE_400_INVALID_INPUT = "Invalid input data";
    public static final String API_RESPONSE_500_INTERNAL_ERROR = "Internal server error";

    public static final String CALCULOS_API_PATH = "/api/v1/calculos";
    public static final String HISTORY_API_PATH = "/api/v1/history";
    public static final String HISTORY_TAG_NAME = "Historial de Llamadas";
    public static final String HISTORY_TAG_DESCRIPTION = "Operaciones para consultar y registrar el historial de llamadas";
    public static final String HISTORY_OPERATION_SUMMARY = "Obtener historial de llamadas";
    public static final String HISTORY_OPERATION_DESCRIPTION = "Devuelve una lista paginada del historial de llamadas realizadas a los endpoints";
    public static final String EMPTY_BODY_RESPONSE = "{}";
    public static final String SUCCESS = "success";

    public static final String SWAGGER_UI_PATTERN = "/swagger-ui/**";
    public static final String V3_API_DOCS_PATTERN = "/v3/api-docs/**";
    public static final String ACTUATOR_PATTERN = "/actuator/**";
    public static final String ERROR_PATH = "/error";
}
