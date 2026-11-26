package com.expensetracker.network;
import java.io.*;
import java.net.*;
public class ExpenseServer implements AutoCloseable {
    private final ServerSocket serverSocket;
    private final Thread serverThread;
    private volatile boolean running = true;
    public ExpenseServer() throws IOException {
        serverSocket = new ServerSocket(0);
        serverThread = new Thread(this::listen, "expense-server");
        serverThread.start();
    }
    public int getPort() {
        return serverSocket.getLocalPort();
    }
    private void listen() {
        while (running) {
            try (Socket client = serverSocket.accept();
                 BufferedReader input = new BufferedReader(
                         new InputStreamReader(client.getInputStream()));
                 PrintWriter output = new PrintWriter(
                         client.getOutputStream(), true)) {
                String message = input.readLine();
                if ("PING".equals(message)) {
                    output.println("PONG");
                } else {
                    output.println("UNKNOWN");
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void close() {
        running = false;
        try {
            serverSocket.close();
            serverThread.join(2000);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}