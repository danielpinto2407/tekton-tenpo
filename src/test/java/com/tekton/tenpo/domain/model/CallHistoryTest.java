package com.tekton.tenpo.domain.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.time.Instant;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

public class CallHistoryTest {

    @Test
    public void generatedValueStrategyIsIdentity() throws NoSuchFieldException {
        Field idField = CallHistory.class.getDeclaredField("id");
        assertNotNull("id field must exist", idField);

        GeneratedValue generatedValue = idField.getAnnotation(GeneratedValue.class);
        assertNotNull("id field should be annotated with @GeneratedValue", generatedValue);
        assertEquals("Generation strategy should be IDENTITY", GenerationType.IDENTITY, generatedValue.strategy());
    }

    @Test
    public void lombokBuilderAndAccessorsWork() {
        Instant now = Instant.now();
        CallHistory ch = CallHistory.builder()
                .id(42L)
                .timestamp(now)
                .endpoint("/api/test")
                .parameters("{\"a\":1}")
                .response("{\"ok\":true}")
                .statusCode(200)
                .build();

        assertEquals(Long.valueOf(42L), ch.getId());
        assertEquals(now, ch.getTimestamp());
        assertEquals("/api/test", ch.getEndpoint());
        assertEquals("{\"a\":1}", ch.getParameters());
        assertEquals("{\"ok\":true}", ch.getResponse());
        assertEquals(Integer.valueOf(200), ch.getStatusCode());
    }
}