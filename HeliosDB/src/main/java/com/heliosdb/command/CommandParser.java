package com.heliosdb.command;

public class CommandParser {

    private final CommandRegistry registry;

    public CommandParser(CommandRegistry registry) {
        this.registry = registry;
    }


    public void setReplayMode(boolean mode) {
        registry.setReplayMode(mode);
    }

    public String parse(String input) {

        try {
            String[] parts = input.split(" ");
            String command = parts[0].toUpperCase();

            switch (command) {

                case "SET":
                    return registry.getService().set(parts[1], parts[2]);

                case "GET":
                    return registry.getService().get(parts[1]);

                case "DEL":
                    return registry.getService().delete(parts[1]) ? "1" : "0";

                case "EXISTS":
                    return registry.getService().exists(parts[1]) ? "1" : "0";

                case "SETEX":
                    registry.getService().set(parts[1], parts[3], Long.parseLong(parts[2]));
                    return "OK";

                default:
                    return "ERROR: Unknown command";
            }

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}