package com.tekton.tenpo.infrastructure.adapters.out.persistence;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CallHistoryEntityTest {

    @Test
    void createEntity_withAllFields_succeeds() {
        Instant now = Instant.now();
        CallHistoryEntity entity = new CallHistoryEntity(1L, now, "/api/test", "param1=value1", "response_body", 200);

        assertEquals(1L, entity.getId());
        assertEquals(now, entity.getTimestamp());
        assertEquals("/api/test", entity.getEndpoint());
        assertEquals("param1=value1", entity.getParameters());
        assertEquals("response_body", entity.getResponse());
        assertEquals(200, entity.getStatusCode());
    }

    @Test
    void buildEntity_usingBuilder_succeeds() {
        Instant now = Instant.now();
        CallHistoryEntity entity = CallHistoryEntity.builder()
                .id(2L)
                .timestamp(now)
                .endpoint("/api/history")
                .parameters("page=0&size=10")
                .response("{\"data\": []}")
                .statusCode(200)
                .build();

        assertEquals(2L, entity.getId());
        assertEquals(now, entity.getTimestamp());
        assertEquals("/api/history", entity.getEndpoint());
        assertEquals("page=0&size=10", entity.getParameters());
        assertEquals("{\"data\": []}", entity.getResponse());
        assertEquals(200, entity.getStatusCode());
    }

    @Test
    void createEntity_withMinimalFields_succeeds() {
        Instant now = Instant.now();
        CallHistoryEntity entity = CallHistoryEntity.builder()
                .timestamp(now)
                .endpoint("/api/test")
                .statusCode(404)
                .build();

        assertNull(entity.getId());
        assertEquals(now, entity.getTimestamp());
        assertEquals("/api/test", entity.getEndpoint());
        assertNull(entity.getParameters());
        assertNull(entity.getResponse());
        assertEquals(404, entity.getStatusCode());
    }

    @Test
    void setters_updateFields_correctly() {
        CallHistoryEntity entity = new CallHistoryEntity();
        Instant now = Instant.now();

        entity.setId(3L);
        entity.setTimestamp(now);
        entity.setEndpoint("/api/new");
        entity.setParameters("newParam=value");
        entity.setResponse("newResponse");
        entity.setStatusCode(500);

        assertEquals(3L, entity.getId());
        assertEquals(now, entity.getTimestamp());
        assertEquals("/api/new", entity.getEndpoint());
        assertEquals("newParam=value", entity.getParameters());
        assertEquals("newResponse", entity.getResponse());
        assertEquals(500, entity.getStatusCode());
    }

    @Test
    void noArgsConstructor_createsEmptyEntity() {
        CallHistoryEntity entity = new CallHistoryEntity();

        assertNull(entity.getId());
        assertNull(entity.getTimestamp());
        assertNull(entity.getEndpoint());
        assertNull(entity.getParameters());
        assertNull(entity.getResponse());
        assertNull(entity.getStatusCode());
    }

    @Test
    void entity_withVariousStatusCodes() {
        CallHistoryEntity success = CallHistoryEntity.builder()
                .statusCode(200)
                .endpoint("/api/test")
                .timestamp(Instant.now())
                .build();

        CallHistoryEntity created = CallHistoryEntity.builder()
                .statusCode(201)
                .endpoint("/api/create")
                .timestamp(Instant.now())
                .build();

        CallHistoryEntity notFound = CallHistoryEntity.builder()
                .statusCode(404)
                .endpoint("/api/notfound")
                .timestamp(Instant.now())
                .build();

        CallHistoryEntity serverError = CallHistoryEntity.builder()
                .statusCode(500)
                .endpoint("/api/error")
                .timestamp(Instant.now())
                .build();

        assertEquals(200, success.getStatusCode());
        assertEquals(201, created.getStatusCode());
        assertEquals(404, notFound.getStatusCode());
        assertEquals(500, serverError.getStatusCode());
    }
}
