package com.heliosdb.persistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AOFLogger {

    private final BufferedWriter writer;

    public AOFLogger(String filePath) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(filePath, true));

        // Background flush thread
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    flush();
                } catch (Exception ignored) {}
            }
        }, "aof-flush-thread").start();
    }

    public void log(String command) {
        try {
            writer.write(command);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("AOF write failed");
        }
    }

    public synchronized void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            System.out.println("Flush failed");
        }
    }

    public synchronized void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Close failed");
        }
    }
}