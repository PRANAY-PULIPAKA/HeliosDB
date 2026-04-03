package com.heliosdb.store;

import com.heliosdb.ttl.ExpiryEntry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class InMemoryStore implements KeyValueStore {

    private final Map<String, ValueWrapper> store = new ConcurrentHashMap<>();

    // Thread-safe Priority Queue
    private final PriorityBlockingQueue<ExpiryEntry> expiryQueue =
            new PriorityBlockingQueue<>();

    @Override
    public void set(String key, String value) {
        store.put(key, new ValueWrapper(value, -1));
    }

    @Override
    public void set(String key, String value, long ttlMillis) {
        long expiryTime = System.currentTimeMillis() + ttlMillis;

        store.put(key, new ValueWrapper(value, expiryTime));

        // Push into heap
        expiryQueue.add(new ExpiryEntry(key, expiryTime));
    }

    @Override
    public String get(String key) {

        ValueWrapper wrapper = store.get(key);

        if (wrapper == null) return null;

        long now = System.currentTimeMillis();

        if (wrapper.getExpiryTime() != -1 &&
                now > wrapper.getExpiryTime()) {

            store.remove(key); // lazy delete
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

    @Override
    public int size() {
        return store.size();
    }

    // OPTIMIZED CLEANUP (NO FULL SCAN)
    public void cleanUpExpiredKeys() {

        long now = System.currentTimeMillis();

        while (!expiryQueue.isEmpty()) {

            ExpiryEntry entry = expiryQueue.peek();

            if (entry.getExpiryTime() > now) {
                break; // heap top not expired → stop
            }

            expiryQueue.poll();

            ValueWrapper wrapper = store.get(entry.getKey());

            if (wrapper != null &&
                    wrapper.getExpiryTime() == entry.getExpiryTime()) {

                store.remove(entry.getKey());
            }
        }
    }
}