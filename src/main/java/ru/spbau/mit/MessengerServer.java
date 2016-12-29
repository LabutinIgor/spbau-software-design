package ru.spbau.mit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The MessengerServer class provides realisation of server that can send and receive messages
 */
public class MessengerServer {
    private MessagesReceiver messagesReceiver;
    private int port;
    private Connection connection = null;

    public MessengerServer(int port, MessengerGUIMain messengerGUIMain) {
        this.port = port;
        this.messagesReceiver = messengerGUIMain;
    }

    /**
     * This method starts server and receives messages
     */
    public synchronized void start() throws IOException {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket socket = serverSocket.accept();
                connection = new Connection(socket.getInputStream(), socket.getOutputStream(), messagesReceiver);
                connection.start();
            } catch (IOException ignored) {
            }
        }).start();
    }

    /**
     * This method sends message to client
     */
    public synchronized void sendMessage(String name, String message) throws IOException {
        if (connection == null) {
            throw new IOException("No client connected");
        }
        connection.sendMessage(name, message);
    }

    /**
     * This method stops server
     */
    public synchronized void stop() {
        connection.stop();
    }
}
