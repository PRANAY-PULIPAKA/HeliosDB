package com.heliosdb.store;
import java.util.Set;

public interface KeyValueStore {

    boolean exists(String key);
    Set<String> keys();

    int size();

    void set(String key, String value);

    void set(String key, String value, long ttlMillis);

    String get(String key);

    boolean delete(String key);
}