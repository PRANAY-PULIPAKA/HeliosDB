package com.heliosdb;

import com.heliosdb.command.CommandParser;
import com.heliosdb.command.CommandRegistry;
import com.heliosdb.persistence.AOFLoader;
import com.heliosdb.persistence.AOFLogger;
import com.heliosdb.service.KeyValueService;
import com.heliosdb.store.InMemoryStore;
import com.heliosdb.tcp.TCPServer;
import com.heliosdb.ttl.TTLManager;

import java.io.File;

public class HeliosDbApplication {

    public static void main(String[] args) throws Exception {

        System.out.println("Application starting...");

        String filePath = "aof.log";

        boolean FAST_START = false;

        System.out.println("📂 AOF absolute path: " + new File(filePath).getAbsolutePath());

        InMemoryStore store = new InMemoryStore();

        // Persistence
        AOFLogger logger = new AOFLogger(filePath);
        KeyValueService service = new KeyValueService(store, logger);

        // Command layer
        CommandRegistry registry = new CommandRegistry(service);
        CommandParser parser = new CommandParser(registry);


        try {
            AOFLoader loader = new AOFLoader(filePath, parser);
            loader.load(FAST_START);
        } catch (Exception e) {
            System.out.println("Failed to load AOF: " + e.getMessage());
        }

        // TTL
        TTLManager ttlManager = new TTLManager(store);
        ttlManager.start();

        // Start server
        System.out.println("Starting TCP Server...");
        TCPServer server = new TCPServer(6379, parser);
        server.start();

        System.out.println("Server started on port 6379");


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            logger.close();
        }));
    }
}