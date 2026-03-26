package com.heliosdb.command.impl;

import com.heliosdb.command.Command;
import com.heliosdb.service.KeyValueService;

import java.util.Set;

public class KeysCommand implements Command {

    private final KeyValueService service;

    public KeysCommand(KeyValueService service, String[] tokens) {
        this.service = service;

        if (tokens.length != 2 || !tokens[1].equals("*")) {
            throw new IllegalArgumentException("KEYS *");
        }
    }

    @Override
    public String execute() {
        Set<String> keys = service.keys();
        return String.join(" ", keys);
    }
}