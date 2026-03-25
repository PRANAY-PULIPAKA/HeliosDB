package com.heliosdb.service;

import com.heliosdb.store.KeyValueStore;

import java.util.Set;
import java.util.stream.Collectors;

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

        long ttlMillis = ttlSeconds * 1000;
        store.set(key, value, ttlMillis);
    }

    public String get(String key) {
        validateKey(key);
        return store.get(key); // handles lazy expiry
    }

    public boolean delete(String key) {
        validateKey(key);
        return store.delete(key);
    }

    public boolean exists(String key) {
        validateKey(key);
        return store.exists(key); // internally uses get()
    }

    public Set<String> keys() {
        return store.keys().stream()
                .filter(key -> store.get(key) != null) // removes expired keys
                .collect(Collectors.toSet());
    }

    public int size() {
        return (int) store.keys().stream()
                .filter(key -> store.get(key) != null) // removes expired
                .count();
    }

    private void validateKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
    }
}