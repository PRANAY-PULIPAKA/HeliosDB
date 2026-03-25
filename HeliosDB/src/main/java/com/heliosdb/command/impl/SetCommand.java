package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class SetCommand implements Command {

    private final KeyValueService service;

    public SetCommand(KeyValueService service) {
        this.service = service;
    }

    @Override
    public String execute(String[] args) {

        if (args.length < 3) {
            return "ERROR: SET key value [TTL]";
        }

        String key = args[1];
        String value = args[2];

        if (args.length == 4) {
            long ttl = Long.parseLong(args[3]);
            service.set(key, value, ttl);
        } else {
            service.set(key, value);
        }

        return "OK";
    }
}