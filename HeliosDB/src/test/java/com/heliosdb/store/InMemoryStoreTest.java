package com.heliosdb.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStoreTest {

    private InMemoryStore store;

    @BeforeEach
    void setup() {
        store = new InMemoryStore();
    }

    @Test
    void testSetAndGet() {
        store.set("key", "value");
        assertEquals("value", store.get("key"));
    }

    @Test
    void testTTLExpiry() throws InterruptedException {
        store.set("k", "v", 1000);
        Thread.sleep(1200);
        assertNull(store.get("k"));
    }
}