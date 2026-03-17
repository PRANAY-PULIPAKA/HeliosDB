package com.heliosdb.tcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private final int port;

    public TCPServer(int port) {
        this.port = port;
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("HeliosDB TCP Server started on port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println("Client connected: " + clientSocket.getInetAddress());

                handleClient(clientSocket);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleClient(Socket clientSocket) {

        try {

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            PrintWriter writer =
                    new PrintWriter(clientSocket.getOutputStream(), true);

            String line;

            while ((line = reader.readLine()) != null) {

                System.out.println("Received: " + line);

                writer.println("Echo: " + line);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}