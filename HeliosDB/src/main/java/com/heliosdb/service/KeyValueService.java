package com.heliosdb.service;

import com.heliosdb.store.KeyValueStore;

public class KeyValueService {

    private final KeyValueStore store;

    public KeyValueService(KeyValueStore store) {
        this.store = store;
    }

    public void set(String key, String value) {
        validateKey(key);
        store.set(key, value);
    }

    public void set(String key, String value, long ttlSeconds) {
        validateKey(key);

        if (ttlSeconds <= 0) {
            throw new IllegalArgumentException("TTL must be positive");
        }

        System.out.println(" SERVICE CALLED TTL seconds = " + ttlSeconds);

        long ttlMillis = ttlSeconds * 1000;

        System.out.println("TTL millis = " + ttlMillis);

        store.set(key, value, ttlMillis);
    }

    public String get(String key) {
        validateKey(key);
        return store.get(key);
    }

    public void delete(String key) {
        validateKey(key);
        store.delete(key);
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
    }
}