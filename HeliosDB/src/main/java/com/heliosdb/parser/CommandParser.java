package com.heliosdb.parser;

import com.heliosdb.service.KeyValueService;

public class CommandParser {

    private final KeyValueService service;

    public CommandParser(KeyValueService service) {
        this.service = service;
    }

    public String parse(String input) {

        if (input == null || input.trim().isEmpty()) {
            return "ERROR: Empty command";
        }

        String[] tokens = input.trim().split("\\s+");

        String command = tokens[0].toUpperCase();

        switch (command) {

            case "SET":
                return handleSet(tokens);

            case "GET":
                return handleGet(tokens);

            case "DELETE":
                return handleDelete(tokens);

            default:
                return "ERROR: Unknown command";
        }
    }

    private String handleSet(String[] tokens) {

        if (tokens.length < 3) {
            return "ERROR: SET requires key and value";
        }

        String key = tokens[1];
        String value = tokens[2];

        if (tokens.length == 4) {
            long ttl = Long.parseLong(tokens[3]);
            service.set(key, value, ttl);
        } else {
            service.set(key, value);
        }

        return "OK";
    }

    private String handleGet(String[] tokens) {

        if (tokens.length != 2) {
            return "ERROR: GET requires key";
        }

        String value = service.get(tokens[1]);

        return value == null ? "(nil)" : value;
    }

    private String handleDelete(String[] tokens) {

        if (tokens.length != 2) {
            return "ERROR: DELETE requires key";
        }

        service.delete(tokens[1]);

        return "OK";
    }
}