package com.heliosdb.store;

public interface KeyValueStore {

    void set(String key, String value);

    void set(String key, String value, long ttlMillis);

    String get(String key);

    void delete(String key);
}