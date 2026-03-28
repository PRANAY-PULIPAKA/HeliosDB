package com.heliosdb.persistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AOFLogger {

    private final BufferedWriter writer;

    public AOFLogger(String filePath) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(filePath, true));
    }

    public synchronized void log(String command) {
        try {
            writer.write(command);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to write to AOF: " + e.getMessage());
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to close AOF logger");
        }
    }
}