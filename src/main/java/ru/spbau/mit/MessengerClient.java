package ru.spbau.mit;

import java.io.IOException;
import java.net.Socket;

public class MessengerClient {
    private MessengerGUIMain messengerGUIMain;
    private String host;
    private int port;
    private Connection connection = null;

    public MessengerClient(String host, int port, MessengerGUIMain messengerGUIMain) {
        this.host = host;
        this.port = port;
        this.messengerGUIMain = messengerGUIMain;
    }

    public synchronized void start() throws IOException {
        new Thread(() -> {
            try {
                Socket socket = new Socket(host, port);
                connection = new Connection(socket, messengerGUIMain);
                connection.start();
            } catch (IOException ignored) {
            }
        }).start();
    }

    public synchronized void sendMessage(String name, String message) throws IOException {
        if (connection == null) {
            throw new IOException("Unable to connect to server");
        }
        connection.sendMessage(name, message);
    }

    public synchronized void stop() {
        connection.stop();
    }
}
