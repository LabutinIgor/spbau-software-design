package ru.spbau.mit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessengerServer {
    private MessengerGUIMain messengerGUIMain;
    private int port;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean isClosed = false;

    public MessengerServer(int port, MessengerGUIMain messengerGUIMain) {
        this.port = port;
        this.messengerGUIMain = messengerGUIMain;
    }

    public synchronized void sendMessage(String message) throws IOException {
        outputStream.writeInt(1);
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    public synchronized void receiveMessages() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket socket = serverSocket.accept();
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
