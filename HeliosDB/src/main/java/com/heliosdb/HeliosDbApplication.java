package com.heliosdb;

import com.heliosdb.parser.CommandParser;
import com.heliosdb.service.KeyValueService;
import com.heliosdb.store.InMemoryStore;
import com.heliosdb.store.KeyValueStore;
import com.heliosdb.tcp.TCPServer;

public class HeliosDbApplication {

    public static void main(String[] args) {

        KeyValueStore store = new InMemoryStore();
        KeyValueService service = new KeyValueService(store);
        CommandParser parser = new CommandParser(service);

        TCPServer server = new TCPServer(6379, parser);
        server.start();
    }
}