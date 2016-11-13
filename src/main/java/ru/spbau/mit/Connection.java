package ru.spbau.mit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean isClosed;
    private MessengerGUIMain messengerGUIMain;

    public Connection(Socket socket, MessengerGUIMain messengerGUIMain) {
        this.socket = socket;
        this.messengerGUIMain = messengerGUIMain;
    }

    public void start() throws IOException {
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
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
    }

    public void sendMessage(String message) throws IOException {
        outputStream.writeInt(1);
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    public void stop() {
        isClosed = true;
    }
}
