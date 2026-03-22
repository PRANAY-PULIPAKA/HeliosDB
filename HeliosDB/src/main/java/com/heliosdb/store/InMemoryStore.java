package com.heliosdb.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStore implements KeyValueStore {

    // Thread-safe map
    private final Map<String, ValueWrapper> store = new ConcurrentHashMap<>();

    @Override
    public void set(String key, String value) {
        store.put(key, new ValueWrapper(value, -1));
    }

    @Override
    public void set(String key, String value, long ttlMillis) {

        long current = System.currentTimeMillis();
        long expiryTime = current + ttlMillis;

        System.out.println("STORE TTL millis = " + ttlMillis);
        System.out.println("Current time = " + current);
        System.out.println("Expiry time = " + expiryTime);

        store.put(key, new ValueWrapper(value, expiryTime));
    }

    @Override
    public String get(String key) {

        ValueWrapper wrapper = store.get(key);

        if (wrapper == null) return null;

        long now = System.currentTimeMillis();

        System.out.println("GET key = " + key);
        System.out.println("NOW = " + now);
        System.out.println("EXPIRY = " + wrapper.getExpiryTime());


        if (wrapper.getExpiryTime() != -1 && now > wrapper.getExpiryTime()) {
            System.out.println("KEY EXPIRED");
            store.remove(key);
            return null;
        }

        return wrapper.getValue();
    }

    @Override
    public void delete(String key) {
        store.remove(key);
    }
}