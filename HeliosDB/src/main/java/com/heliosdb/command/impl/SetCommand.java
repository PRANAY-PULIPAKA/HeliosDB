package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class SetCommand implements Command {

    private final KeyValueService service;
    private final String key;
    private final String value;
    private final Long ttl;

    public SetCommand(KeyValueService service, String[] tokens) {
        this.service = service;

        if (tokens.length < 3) {
            throw new IllegalArgumentException("SET key value [TTL]");
        }

        this.key = tokens[1];
        this.value = tokens[2];

        if (tokens.length == 4) {
            this.ttl = Long.parseLong(tokens[3]);
        } else {
            this.ttl = null;
        }
    }

    @Override
    public String execute() {
        if (ttl != null) {
            service.set(key, value, ttl);
        } else {
            service.set(key, value);
        }
        return "OK";
    }
}