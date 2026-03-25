package com.heliosdb.command;

import com.heliosdb.command.impl.*;
import com.heliosdb.service.KeyValueService;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, Command> commands = new HashMap<>();

    public CommandRegistry(KeyValueService service) {
        commands.put("SET", new SetCommand(service));
        commands.put("GET", new GetCommand(service));
        commands.put("DEL", new DelCommand(service));
        commands.put("EXISTS", new ExistsCommand(service));
        commands.put("KEYS", new KeysCommand(service));
        commands.put("SIZE", new SizeCommand(service));
    }

    public Command getCommand(String name) {
        return commands.get(name.toUpperCase());
    }
}