package com.heliosdb;

import com.heliosdb.command.CommandParser;
import com.heliosdb.command.CommandRegistry;
import com.heliosdb.service.KeyValueService;
import com.heliosdb.store.InMemoryStore;
import com.heliosdb.tcp.TCPServer;
import com.heliosdb.ttl.TTLManager;

public class HeliosDbApplication {

    public static void main(String[] args) {

        InMemoryStore store = new InMemoryStore();

        KeyValueService service = new KeyValueService(store);
        CommandRegistry registry = new CommandRegistry(service);
        CommandParser parser = new CommandParser(registry);

        // START TTL CLEANER
        TTLManager ttlManager = new TTLManager(store);
        ttlManager.start();

        TCPServer server = new TCPServer(6379, parser);
        server.start();
    }
}