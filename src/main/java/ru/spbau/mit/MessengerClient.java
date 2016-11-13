package ru.spbau.mit;

import java.io.IOException;
import java.net.Socket;

public class MessengerClient {
    private MessengerGUIMain messengerGUIMain;
    private String host;
    private int port;
    private Connection connection;

    public MessengerClient(String host, int port, MessengerGUIMain messengerGUIMain) {
        this.host = host;
        this.port = port;
        this.messengerGUIMain = messengerGUIMain;
    }

    public synchronized void sendMessage(String message) throws IOException {
        connection.sendMessage(message);
    }

    public synchronized void start() throws IOException {
        Socket socket = new Socket(host, port);
        connection = new Connection(socket, messengerGUIMain);
        new Thread(() -> {
            try {
                connection.start();
            } catch (IOException ignored) {
            }
        }).start();
    }

    public synchronized void stop() {
        connection.stop();
    }
}
