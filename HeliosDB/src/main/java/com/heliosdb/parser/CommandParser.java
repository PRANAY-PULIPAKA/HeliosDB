package com.heliosdb.parser;

public class CommandParser {

        public String parse(String input) {

            if (input == null || input.isEmpty()) {
                return "ERROR: Empty command";
            }

            String[] tokens = input.split(" ");

            String command = tokens[0].toUpperCase();

            switch (command) {

                case "SET":
                    return handleSet(tokens);

                case "GET":
                    return handleGet(tokens);

                case "DELETE":
                    return handleDelete(tokens);

                default:
                    return "ERROR: Unknown command";
            }
        }

        private String handleSet(String[] tokens) {

            if (tokens.length != 3) {
                return "ERROR: SET requires key and value";
            }

            String key = tokens[1];
            String value = tokens[2];

            // Temporary (real storage comes Day 3)
            return "OK (SET " + key + " = " + value + ")";
        }

        private String handleGet(String[] tokens) {

            if (tokens.length != 2) {
                return "ERROR: GET requires key";
            }

            String key = tokens[1];

            // Temporary
            return "VALUE of " + key;
        }

        private String handleDelete(String[] tokens) {

            if (tokens.length != 2) {
                return "ERROR: DELETE requires key";
            }

            String key = tokens[1];

            // Temporary
            return "DELETED " + key;
        }
}

