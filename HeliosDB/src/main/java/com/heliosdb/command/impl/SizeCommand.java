package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class SizeCommand implements Command {

    private final KeyValueService service;

    public SizeCommand(KeyValueService service, String[] tokens) {
        this.service = service;

        if (tokens.length != 1) {
            throw new IllegalArgumentException("SIZE");
        }
    }

    @Override
    public String execute() {
        return "(integer) " + service.size();
    }
}