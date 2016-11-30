package ru.spbau.mit;

import java.io.*;

public class Connection {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean isClosed;
    private MessagesReceiver messengerGUIMain;

    public Connection(InputStream inputStream, OutputStream outputStream, MessagesReceiver messengerGUIMain) {
        this.inputStream = new DataInputStream(inputStream);
        this.outputStream = new DataOutputStream(outputStream);
        this.messengerGUIMain = messengerGUIMain;
    }

    public void start() throws IOException {
        while (!isClosed) {
            int query = inputStream.readInt();
            if (query == 1) {
                String name = inputStream.readUTF();
                String message = inputStream.readUTF();
                messengerGUIMain.receiveMessage(name, message);
            } else {
                isClosed = true;
                break;
            }
        }
    }

    public void sendMessage(String name, String message) throws IOException {
        outputStream.writeInt(1);
        outputStream.writeUTF(name);
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    public void stop() {
        isClosed = true;
    }
}
