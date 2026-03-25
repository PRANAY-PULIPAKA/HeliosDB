package com.heliosdb.tcp;

import com.heliosdb.command.CommandParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private final int port;
    private final CommandParser parser;

    public TCPServer(int port, CommandParser parser) {
        this.port = port;
        this.parser = parser;
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {

        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true)
        ) {

            String line;

            while ((line = reader.readLine()) != null) {
                String response = parser.parse(line);
                writer.println(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}