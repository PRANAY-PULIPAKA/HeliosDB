package com.heliosdb.service;

import com.heliosdb.persistence.AOFLogger;
import com.heliosdb.store.KeyValueStore;

import java.util.Set;
import java.util.stream.Collectors;

public class KeyValueService {

    private final KeyValueStore store;
    private final AOFLogger logger;

    public KeyValueService(KeyValueStore store, AOFLogger logger) {
        this.store = store;
        this.logger = logger;
    }

    public String set(String key, String value) {
        validateKey(key);
        store.set(key, value);

        logger.log("SET " + key + " " + value);

        return "OK";
    }

    public void set(String key, String value, long ttlSeconds) {
        validateKey(key);

        if (ttlSeconds <= 0) {
            throw new IllegalArgumentException("TTL must be positive");
        }

        long ttlMillis = ttlSeconds * 1000;
        store.set(key, value, ttlMillis);

        logger.log("SETEX " + key + " " + ttlSeconds + " " + value);
    }

    public String get(String key) {
        validateKey(key);
        return store.get(key);
    }

    public boolean delete(String key) {
        validateKey(key);
        boolean result = store.delete(key);

        if (result) {
            logger.log("DEL " + key); // logging delete
        }

        return result;
    }

    public boolean exists(String key) {
        validateKey(key);
        return store.exists(key);
    }

    public Set<String> keys() {
        return store.keys().stream()
                .filter(key -> store.get(key) != null)
                .collect(Collectors.toSet());
    }

    public int size() {
        return (int) store.keys().stream()
                .filter(key -> store.get(key) != null)
                .count();
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
    }
}