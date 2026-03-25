package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class SizeCommand implements Command {

    private final KeyValueService service;

    public SizeCommand(KeyValueService service) {
        this.service = service;
    }

    @Override
    public String execute(String[] args) {

        if (args.length != 1) {
            return "ERROR: SIZE";
        }

        return "(integer) " + service.size();
    }
}