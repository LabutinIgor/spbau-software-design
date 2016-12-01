package ru.spbau.mit;

/**
 * The MessagesReceiver interface is interface for view that can receive messages,
 * client and server uses it
 */
public interface MessagesReceiver {

    /**
     * This method handle received messages
     */
    void receiveMessage(String name, String message);

    /**
     * This method handle typing for notification user about it
     */
    void receiveTypingNotification(String name);

}
