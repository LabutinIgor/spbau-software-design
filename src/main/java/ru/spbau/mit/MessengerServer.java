package ru.spbau.mit;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessengerServer {
    private MessengerGUIMain messengerGUIMain;
    private int port;
    private Connection connection;

    public MessengerServer(int port, MessengerGUIMain messengerGUIMain) {
        this.port = port;
        this.messengerGUIMain = messengerGUIMain;
    }

    public synchronized void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
        connection = new Connection(socket, messengerGUIMain);
        new Thread(() -> {
            try {
                connection.start();
            } catch (IOException ignored) {
            }
        }).start();
    }

    public synchronized void sendMessage(String message) throws IOException {
        connection.sendMessage(message);
    }

    public synchronized void stop() {
        connection.stop();
    }
}
