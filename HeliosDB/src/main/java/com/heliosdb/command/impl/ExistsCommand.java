package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class ExistsCommand implements Command {

    private final KeyValueService service;

    public ExistsCommand(KeyValueService service) {
        this.service = service;
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 2) return "ERROR";

        return service.exists(args[1]) ? "(integer) 1" : "(integer) 0";
    }
}