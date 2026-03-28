package com.heliosdb.persistence;

import com.heliosdb.command.CommandParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AOFLoader {

    private final String filePath;
    private final CommandParser parser;

    private static final int MAX_AOF_LOAD = 5000;

    public AOFLoader(String filePath, CommandParser parser) {
        this.filePath = filePath;
        this.parser = parser;
    }

    public void load(boolean fastStart) {

        File file = new File(filePath);

        System.out.println("📂 AOF absolute path: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("No AOF file found. Skipping load.");
            return;
        }

        if (fastStart) {
            System.out.println(" Fast start enabled. Skipping AOF load.");
            return;
        }

        System.out.println("Loading AOF...");

        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                parser.parse(line);
                count++;

                if (count >= MAX_AOF_LOAD) {
                    System.out.println("Too many commands, stopping load...");
                    break;
                }
            }

            System.out.println("Loaded " + count + " commands from AOF");

        } catch (Exception e) {
            System.out.println("Error loading AOF: " + e.getMessage());
        }
    }
}