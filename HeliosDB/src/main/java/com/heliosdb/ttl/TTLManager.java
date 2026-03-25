package com.heliosdb.ttl;

import com.heliosdb.store.InMemoryStore;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TTLManager {

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setName("ttl-cleaner-thread");
                t.setDaemon(true);
                return t;
            });

    private final InMemoryStore store;

    public TTLManager(InMemoryStore store) {
        this.store = store;
    }

    public void start() {

        scheduler.scheduleWithFixedDelay(() -> {
            try {
                store.cleanUpExpiredKeys();
            } catch (Exception e) {
                System.out.println("TTL cleanup error: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }
}