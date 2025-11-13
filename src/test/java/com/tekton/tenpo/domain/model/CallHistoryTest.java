package com.tekton.tenpo.domain.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Instant;

public class CallHistoryTest {

    @Test
    public void recordConstructorAndAccessorsWork() {
      
        Long id = 42L;
        Instant now = Instant.now();
        String endpoint = "/api/test";
        String parameters = "{\"a\":1}";
        String response = "{\"ok\":true}";
        Integer statusCode = 200;

        CallHistory ch = new CallHistory(id, now, endpoint, parameters, response, statusCode);

        assertEquals(id, ch.id());
        assertEquals(now, ch.timestamp());
        assertEquals(endpoint, ch.endpoint());
        assertEquals(parameters, ch.parameters());
        assertEquals(response, ch.response());
        assertEquals(statusCode, ch.statusCode());
    }

    @Test
    public void recordIsImmutable() {
        Instant now = Instant.now();
        CallHistory ch1 = new CallHistory(1L, now, "/api/test", "{}", "{}", 200);
        CallHistory ch2 = new CallHistory(1L, now, "/api/test", "{}", "{}", 200);

        assertEquals(ch1, ch2);
        assertEquals(ch1.hashCode(), ch2.hashCode());
    }
}
