package com.heliosdb.command;

public class CommandParser {

    private final CommandRegistry registry;

    public CommandParser(CommandRegistry registry) {
        this.registry = registry;
    }

    public String parse(String input) {

        try {
            // 1. Validate input
            if (input == null || input.trim().isEmpty()) {
                return "ERROR: Empty command";
            }

            // 2. Tokenize
            String[] parts = input.trim().split("\\s+");

            // 3. Fetch command
            Command command = registry.getCommand(parts[0]);

            if (command == null) {
                return "ERROR: Unknown command";
            }

            // 4. Execute
            return command.execute(parts);

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}