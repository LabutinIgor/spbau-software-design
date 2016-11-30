package ru.spbau.mit;

/**
 * The MessagesReceiver interface is interface for view that can receive messages,
 * client and server uses it
 */
public interface MessagesReceiver {

    /**
     * This method handle received messages,
     * server and client can call only this method of view
     */
    void receiveMessage(String name, String message);
}
