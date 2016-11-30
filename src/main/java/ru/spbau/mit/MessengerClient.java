package ru.spbau.mit;

import java.io.IOException;
import java.net.Socket;

/**
 * The MessengerClient class provides realisation of client that can send and receive messages
 */
public class MessengerClient {
    private MessagesReceiver messagesReceiver;
    private String host;
    private int port;
    private Connection connection = null;

    public MessengerClient(String host, int port, MessengerGUIMain messengerGUIMain) {
        this.host = host;
        this.port = port;
        this.messagesReceiver = messengerGUIMain;
    }

    /**
     * This method starts client and receives messages
     */
    public synchronized void start() throws IOException {
        new Thread(() -> {
            try {
                Socket socket = new Socket(host, port);
                connection = new Connection(socket.getInputStream(), socket.getOutputStream(), messagesReceiver);
                connection.start();
            } catch (IOException ignored) {
            }
        }).start();
    }

    /**
     * This method sends message to server
     */
    public synchronized void sendMessage(String name, String message) throws IOException {
        if (connection == null) {
            throw new IOException("Unable to connect to server");
        }
        connection.sendMessage(name, message);
    }

    /**
     * This method stops client
     */
    public synchronized void stop() {
        connection.stop();
    }
}
