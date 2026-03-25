package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class GetCommand implements Command {

    private final KeyValueService service;

    public GetCommand(KeyValueService service) {
        this.service = service;
    }

    @Override
    public String execute(String[] args) {

        if (args.length != 2) {
            return "ERROR: GET key";
        }

        String value = service.get(args[1]);

        return value == null ? "NULL" : value;
    }
}