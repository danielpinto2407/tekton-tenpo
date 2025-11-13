package com.tekton.tenpo.infrastructure.adapters.in.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PercentageApiPropertiesTest {

    private PercentageApiProperties properties;

    @BeforeEach
    void setUp() {
        properties = new PercentageApiProperties();
    }

    @Test
    void properties_canBeSet() {
        properties.setBaseUrl("https://example.com");
        properties.setPath("/api/random");

        assertEquals("https://example.com", properties.getBaseUrl());
        assertEquals("/api/random", properties.getPath());
    }

    @Test
    void properties_initiallyNull() {
        assertNull(properties.getBaseUrl());
        assertNull(properties.getPath());
    }

    @Test
    void baseUrl_canBeChanged() {
        properties.setBaseUrl("https://api.example.com");
        assertEquals("https://api.example.com", properties.getBaseUrl());

        properties.setBaseUrl("https://new-api.example.com");
        assertEquals("https://new-api.example.com", properties.getBaseUrl());
    }

    @Test
    void path_canBeChanged() {
        properties.setPath("/v1/random");
        assertEquals("/v1/random", properties.getPath());

        properties.setPath("/v2/percentage");
        assertEquals("/v2/percentage", properties.getPath());
    }

    @Test
    void properties_withMultipleValues() {
        properties.setBaseUrl("https://csrng.net");
        properties.setPath("/api/v1/random");

        assertEquals("https://csrng.net", properties.getBaseUrl());
        assertEquals("/api/v1/random", properties.getPath());
    }

    @Test
    void properties_canBeResetToNull() {
        properties.setBaseUrl("https://example.com");
        properties.setPath("/api/random");

        properties.setBaseUrl(null);
        properties.setPath(null);

        assertNull(properties.getBaseUrl());
        assertNull(properties.getPath());
    }

    @Test
    void properties_withEmptyStrings() {
        properties.setBaseUrl("");
        properties.setPath("");

        assertEquals("", properties.getBaseUrl());
        assertEquals("", properties.getPath());
    }
}
