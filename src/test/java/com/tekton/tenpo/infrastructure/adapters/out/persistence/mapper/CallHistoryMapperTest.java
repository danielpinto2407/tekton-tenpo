package com.tekton.tenpo.infrastructure.adapters.out.persistence.mapper;

import com.tekton.tenpo.domain.model.CallHistory;
import com.tekton.tenpo.infrastructure.adapters.out.persistence.CallHistoryEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CallHistoryMapperTest {

    private final CallHistoryMapper mapper = Mappers.getMapper(CallHistoryMapper.class);

    @Test
    void toDomain_and_toEntity_roundTrip() {
        Instant now = Instant.now();
        CallHistoryEntity entity = new CallHistoryEntity();
        entity.setId(10L);
        entity.setTimestamp(now);
        entity.setEndpoint("/api/test");
        entity.setParameters("a=1&b=2");
        entity.setResponse("{\"ok\":true}");
        entity.setStatusCode(200);

        CallHistory domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals(10L, domain.id());
        assertEquals(now, domain.timestamp());
        assertEquals("/api/test", domain.endpoint());
        assertEquals("a=1&b=2", domain.parameters());
        assertEquals("{\"ok\":true}", domain.response());
        assertEquals(Integer.valueOf(200), domain.statusCode());

        CallHistoryEntity back = mapper.toEntity(domain);
        assertNotNull(back);
        assertEquals(domain.id(), back.getId());
        assertEquals(domain.endpoint(), back.getEndpoint());
        assertEquals(domain.parameters(), back.getParameters());
        assertEquals(domain.response(), back.getResponse());
        assertEquals(domain.statusCode(), back.getStatusCode());
    }
}
