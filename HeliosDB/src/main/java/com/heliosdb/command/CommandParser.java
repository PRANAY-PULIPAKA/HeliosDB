package com.heliosdb.command;


public class CommandParser {

    private final CommandRegistry registry;

    public CommandParser(CommandRegistry registry) {
        this.registry = registry;
    }

    public String parse(String input) {

        try {
            if (input == null || input.trim().isEmpty()) {
                return "ERROR: Empty command";
            }

            String[] tokens = input.trim().split("\\s+");

            Command command = registry.getCommand(tokens);

            return command.execute();

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}