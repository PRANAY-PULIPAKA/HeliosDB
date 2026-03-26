package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class GetCommand implements Command {

    private final KeyValueService service;
    private final String key;

    public GetCommand(KeyValueService service, String[] tokens) {
        this.service = service;

        if (tokens.length != 2) {
            throw new IllegalArgumentException("GET key");
        }

        this.key = tokens[1];
    }

    @Override
    public String execute() {
        String value = service.get(key);
        return value == null ? "NULL" : value;
    }
}