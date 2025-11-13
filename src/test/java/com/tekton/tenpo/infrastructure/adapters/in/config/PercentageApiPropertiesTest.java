package com.tekton.tenpo.infrastructure.adapters.in.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PercentageApiPropertiesTest {

    @Test
    void properties_canBeSet() {
        PercentageApiProperties props = new PercentageApiProperties();

        props.setBaseUrl("https://example.com");
        props.setPath("/api/random");

        assertEquals("https://example.com", props.getBaseUrl());
        assertEquals("/api/random", props.getPath());
    }
}
