package com.expensetracker;
import com.expensetracker.network.ExpenseServer;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;
class ExpenseServerTest {
    @Test
    void clientReceivesPongFromServer() throws Exception {
        try (ExpenseServer server = new ExpenseServer();
             Socket client = new Socket("127.0.0.1", server.getPort());
             PrintWriter output = new PrintWriter(
                     client.getOutputStream(), true);
             BufferedReader input = new BufferedReader(
                     new InputStreamReader(client.getInputStream()))) {
            client.setSoTimeout(3000);
            output.println("PING");
            assertEquals("PONG", input.readLine());
        }
    }
}