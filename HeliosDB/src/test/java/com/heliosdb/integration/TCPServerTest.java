package com.heliosdb.integration;

import com.heliosdb.HeliosDbApplication;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class TCPServerTest {

    @Test
    void testTCPCommands() throws Exception {

        new Thread(() -> {
            try {
                HeliosDbApplication.main(new String[]{});
            } catch (Exception ignored) {}
        }).start();

        Thread.sleep(2000);

        Socket socket = new Socket("localhost", 6379);

        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        writer.println("SET foo bar");
        assertEquals("OK", reader.readLine());

        writer.println("GET foo");
        assertEquals("bar", reader.readLine());

        socket.close();
    }
}