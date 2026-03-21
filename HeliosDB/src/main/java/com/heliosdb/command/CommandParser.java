package com.heliosdb.command;

import com.heliosdb.service.KeyValueService;

public class CommandParser {

    private final KeyValueService service;

    public CommandParser(KeyValueService service) {
        this.service = service;
    }

    public String parse(String input) {
        try {
            String[] parts = input.split(" ");

            String command = parts[0].toUpperCase();

            switch (command) {

                case "SET":
                    return handleSet(parts);

                case "GET":
                    return handleGet(parts);

                case "DELETE":
                    return handleDelete(parts);

                default:
                    return "ERROR: Unknown command";
            }

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private String handleSet(String[] parts) {
        if (parts.length < 3) {
            return "ERROR: SET requires key and value";
        }

        String key = parts[1];
        String value = parts[2];

        // SET key value ttl
        if (parts.length == 4) {
            long ttl = Long.parseLong(parts[3]);
            service.set(key, value, ttl);
        } else {
            service.set(key, value);
        }

        return "OK";
    }

    private String handleGet(String[] parts) {
        if (parts.length != 2) {
            return "ERROR: GET requires key";
        }

        String value = service.get(parts[1]);

        return value == null ? "(nil)" : value;
    }

    private String handleDelete(String[] parts) {
        if (parts.length != 2) {
            return "ERROR: DELETE requires key";
        }

        service.delete(parts[1]);
        return "OK";
    }
}