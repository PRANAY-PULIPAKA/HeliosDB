package com.heliosdb.command;

import com.heliosdb.command.impl.*;
import com.heliosdb.service.KeyValueService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandRegistry {

    private final Map<String, Function<String[], Command>> registry = new HashMap<>();

    public CommandRegistry(KeyValueService service) {

        registry.put("SET", tokens -> new SetCommand(service, tokens));
        registry.put("GET", tokens -> new GetCommand(service, tokens));
        registry.put("DEL", tokens -> new DelCommand(service, tokens));
        registry.put("EXISTS", tokens -> new ExistsCommand(service, tokens));
        registry.put("KEYS", tokens -> new KeysCommand(service, tokens));
        registry.put("SIZE", tokens -> new SizeCommand(service, tokens));
    }

    public Command getCommand(String[] tokens) {

        if (tokens.length == 0) {
            throw new IllegalArgumentException("Empty command");
        }

        String name = tokens[0].toUpperCase();

        Function<String[], Command> factory = registry.get(name);

        if (factory == null) {
            throw new IllegalArgumentException("Unknown command");
        }

        return factory.apply(tokens);
    }
}