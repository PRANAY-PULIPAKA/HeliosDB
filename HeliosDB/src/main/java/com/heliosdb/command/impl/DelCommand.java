package com.heliosdb.command.impl;
import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;


public class DelCommand implements Command {

    private final KeyValueService service;

    public DelCommand(KeyValueService service) {
        this.service = service;
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 2) return "ERROR";

        boolean deleted = service.delete(args[1]);
        return deleted ? "(integer) 1" : "(integer) 0";
    }
}