package com.heliosdb;

import com.heliosdb.tcp.TCPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HeliosDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeliosDbApplication.class, args);

        TCPServer server = new TCPServer(6379);
        server.start();
    }
}
