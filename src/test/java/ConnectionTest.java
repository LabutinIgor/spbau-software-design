import org.junit.Test;
import ru.spbau.mit.Connection;
import ru.spbau.mit.MessagesReceiver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ConnectionTest {
    @Test
    public void testSendAndReceive() throws IOException {
        List<String> clientMessages = new ArrayList<>();
        List<String> serverMessages = new ArrayList<>();
        MessagesReceiver clientReceiver = (name, message) -> {
            clientMessages.add(name + ": " + message);
        };
        MessagesReceiver serverReceiver = (name, message) -> {
            serverMessages.add(name + ": " + message);
        };
        PipedOutputStream clientOutputStream = new PipedOutputStream();
        PipedOutputStream serverOutputStream = new PipedOutputStream();
        PipedInputStream clientInputStream = new PipedInputStream(serverOutputStream);
        PipedInputStream serverInputStream = new PipedInputStream(clientOutputStream);

        Connection clientConnection = new Connection(clientInputStream, clientOutputStream, clientReceiver);
        Connection serverConnection = new Connection(serverInputStream, serverOutputStream, serverReceiver);

        new Thread(() -> {
            try {
                clientConnection.start();
            } catch (IOException ignored) {
            }
        }
        ).start();
        new Thread(() -> {
            try {
                serverConnection.start();
            } catch (IOException ignored) {
            }
        }
        ).start();

        clientConnection.sendMessage("A", "Hello");
        serverConnection.sendMessage("B", "hello");
        clientConnection.sendMessage("A", "a");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }

        assertSame(1, clientMessages.size());
        assertEquals("B: hello", clientMessages.get(0));
        assertSame(2, serverMessages.size());
        assertEquals("A: Hello", serverMessages.get(0));
        assertEquals("A: a", serverMessages.get(1));
    }
}
