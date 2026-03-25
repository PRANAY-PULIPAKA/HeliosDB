package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

public class KeysCommand implements Command {

    private final KeyValueService service;

    public KeysCommand(KeyValueService service) {
        this.service = service;
    }

    @Override
    public String execute(String[] args) {

        if (args.length != 2 || !args[1].equals("*")) {
            return "ERROR: KEYS *";
        }

        return String.join(" ", service.keys());
    }
}