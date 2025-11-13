package com.tekton.tenpo.infrastructure.adapters.in.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenAPIConfigTest {

    @Test
    void customOpenAPI_returnsConfiguredOpenAPI() {
        OpenAPIConfig config = new OpenAPIConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("Tenpo Calculation API", info.getTitle());
        assertEquals("1.0", info.getVersion());
        assertTrue(info.getDescription().contains("cálculo"));
        assertNotNull(info.getContact());
        assertEquals("Tenpo", info.getContact().getName());
    }
}
