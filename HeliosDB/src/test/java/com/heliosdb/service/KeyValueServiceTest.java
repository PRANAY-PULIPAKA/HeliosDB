package com.heliosdb.service;

import com.heliosdb.persistence.AOFLogger;
import com.heliosdb.store.InMemoryStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyValueServiceTest {

    private KeyValueService service;

    @BeforeEach
    void setup() throws Exception {
        service = new KeyValueService(
                new InMemoryStore(),
                new AOFLogger("test.log")
        );
    }

    @Test
    void testSetGet() {
        service.set("a", "1");
        assertEquals("1", service.get("a"));
    }
}