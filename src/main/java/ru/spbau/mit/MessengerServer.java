package ru.spbau.mit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessengerServer {
    private MessengerGUIMain messengerGUIMain;
    private int port;
    private Connection connection = null;

    public MessengerServer(int port, MessengerGUIMain messengerGUIMain) {
        this.port = port;
        this.messengerGUIMain = messengerGUIMain;
    }

    public synchronized void start() throws IOException {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket socket = serverSocket.accept();
                connection = new Connection(socket, messengerGUIMain);
                connection.start();
            } catch (IOException ignored) {
            }
        }).start();
    }

    public synchronized void sendMessage(String name, String message) throws IOException {
        if (connection == null) {
            throw new IOException("No client connected");
        }
        connection.sendMessage(name, message);
    }

    public synchronized void stop() {
        connection.stop();
    }
}
