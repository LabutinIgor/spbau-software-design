package ru.spbau.mit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MessengerClient {
    private MessengerGUIMain messengerGUIMain;
    private String host;
    private int port;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean isClosed = false;

    public MessengerClient(String host, int port, MessengerGUIMain messengerGUIMain) {
        this.host = host;
        this.port = port;
        this.messengerGUIMain = messengerGUIMain;
    }

    public synchronized void sendMessage(String message) throws IOException {
        outputStream.writeInt(1);
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    public synchronized void receiveMessages() throws IOException {
        Socket socket = new Socket(host, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
            try {
                while (!isClosed) {
                    int query = inputStream.readInt();
                    if (query == 1) {
                        String message = inputStream.readUTF();
                        messengerGUIMain.receiveMessage(message);
                    } else {
                        isClosed = true;
                        break;
                    }
                }
            } catch (IOException ignored) {
            }
        }
        ).start();
    }

    public synchronized void stop() {
        isClosed = true;
    }
}
