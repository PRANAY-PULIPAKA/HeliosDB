package com.heliosdb.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

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
            System.out.println("KEY EXPIRED (lazy)");
            store.remove(key);
            return null;
        }

        return wrapper.getValue();
    }

    @Override
    public boolean delete(String key) {
        return store.remove(key) != null;
    }

    @Override
    public boolean exists(String key) {
        return get(key) != null;
    }

    @Override
    public Set<String> keys() {
        return store.keySet();
    }

    // For testing
    @Override
    public int size() {
        return store.size();
    }

    public void cleanUpExpiredKeys() {

        int removed = 0;
        long now = System.currentTimeMillis();

        for (Map.Entry<String, ValueWrapper> entry : store.entrySet()) {

            ValueWrapper wrapper = entry.getValue();

            if (wrapper.getExpiryTime() != -1 &&
                    now > wrapper.getExpiryTime()) {

                store.remove(entry.getKey());
                removed++;
            }
        }

        if (removed > 0) {
            System.out.println("TTL Cleanup removed: " + removed);
        }
    }
}