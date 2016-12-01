package ru.spbau.mit;

import java.io.*;

/**
 * The Connection class provides realisation of receiving messages from input stream,
 * and sending messages to output stream,
 * that is common for client and server
 */
public class Connection {
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean isClosed;
    private MessagesReceiver messagesReceiver;

    public Connection(InputStream inputStream, OutputStream outputStream, MessagesReceiver messengerGUIMain) {
        this.inputStream = new DataInputStream(inputStream);
        this.outputStream = new DataOutputStream(outputStream);
        this.messagesReceiver = messengerGUIMain;
    }

    /**
     * This method receives messages and calls receiver while connection is not closed
     */
    public void start() throws IOException {
        while (!isClosed) {
            int query = inputStream.readInt();
            if (query == 1) {
                String name = inputStream.readUTF();
                String message = inputStream.readUTF();
                messagesReceiver.receiveMessage(name, message);
            } else {
                isClosed = true;
                break;
            }
        }
    }

    /**
     * This method sends message to output stream
     */
    public void sendMessage(String name, String message) throws IOException {
        outputStream.writeInt(1);
        outputStream.writeUTF(name);
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    /**
     * This method stops connection
     */
    public void stop() {
        isClosed = true;
    }
}
