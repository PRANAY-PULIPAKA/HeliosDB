package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class DelCommand implements Command {

    private final KeyValueService service;
    private final String key;

    public DelCommand(KeyValueService service, String[] tokens) {
        this.service = service;

        if (tokens.length != 2) {
            throw new IllegalArgumentException("DEL key");
        }

        this.key = tokens[1];
    }

    @Override
    public String execute() {
        return service.delete(key) ? "(integer) 1" : "(integer) 0";
    }
}