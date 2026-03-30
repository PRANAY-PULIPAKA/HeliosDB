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
            writer.flush(); // safe for now (later we optimize)
        } catch (IOException e) {
            System.out.println("Failed to write to AOF: " + e.getMessage());
        }
    }

    public synchronized void flush() {
        try {
            writer.flush();
            System.out.println("AOF flushed");
        } catch (IOException e) {
            System.out.println("Flush failed");
        }
    }

    public synchronized void close() {
        try {
            writer.close();
            System.out.println("AOF logger closed");
        } catch (IOException e) {
            System.out.println("Failed to close AOF logger");
        }
    }
}