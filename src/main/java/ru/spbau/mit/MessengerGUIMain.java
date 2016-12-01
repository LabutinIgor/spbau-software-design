package ru.spbau.mit;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The MessengerGUIMain class is view for chatting
 */
public final class MessengerGUIMain implements MessagesReceiver {
    private static Logger logger = Logger.getLogger(MessengerGUIMain.class.getName());
    private static final int PORT = 8081;

    private static MessengerClient client = null;
    private static MessengerServer server = null;

    private JFrame frame;
    private JTextArea messagesTextArea;
    private JTextArea messageToSendTextArea;
    private JLabel labelTyping;
    private String userName = "Default name";
    private String lastCompanionName = "";
    private long lastTypingTime = 0;

    private MessengerGUIMain() {
    }

    /**
     * This static method starts app
     */
    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
        } catch (IOException exception) {
            System.err.println("Could not setup logger configuration: " + exception.toString());
        }
        new MessengerGUIMain().start();
    }

    /**
     * This method sets up all user interface
     */
    private void start() {
        frame = new JFrame("Messenger");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JMenuBar menuBar = buildMenuBar();
        frame.setJMenuBar(menuBar);

        JPanel panel = buildPanel();
        frame.add(panel);

        frame.setSize(800, 600);
        frame.setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                                if (lastTypingTime < System.currentTimeMillis() - 2000) {
                                    labelTyping.setText(" ");
                                } else {
                                    labelTyping.setText(lastCompanionName + " is typing...");
                                }
                           }
                       }, 1000, 1000
        );
    }

    /**
     * This method sets up elements of menu bar
     */
    private JMenuBar buildMenuBar() {
        JMenu fileMenu = new JMenu("menu");

        JMenuItem startItem = new JMenuItem("start as server");
        startItem.addActionListener(e -> startServer());
        fileMenu.add(startItem);

        JMenuItem connectItem = new JMenuItem("connect");
        connectItem.addActionListener(e -> startClient());
        fileMenu.add(connectItem);

        JMenuItem setNameItem = new JMenuItem("set name");
        setNameItem.addActionListener(e -> setName());
        fileMenu.add(setNameItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        return menuBar;
    }

    /**
     * This method sets up main panel of view with text areas and "send" button
     */
    private JPanel buildPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        messagesTextArea = new JTextArea();
        messagesTextArea.setEditable(false);
        messagesTextArea.setSize(250, 150);
        ScrollPane scrollPaneMessages = new ScrollPane();
        scrollPaneMessages.add(messagesTextArea);
        scrollPaneMessages.setSize(300, 200);
        panel.add(scrollPaneMessages);

        labelTyping = new JLabel(" ");
        panel.add(labelTyping);

        JLabel labelNewMessage = new JLabel("New message:");
        panel.add(labelNewMessage);

        messageToSendTextArea = new JTextArea();
        messageToSendTextArea.setSize(200, 50);
        messageToSendTextArea.setBackground(Color.cyan);
        messageToSendTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                sendTypingNotification();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        ScrollPane scrollPaneMessagesToSend = new ScrollPane();
        scrollPaneMessagesToSend.add(messageToSendTextArea);
        scrollPaneMessagesToSend.setSize(250, 60);

        panel.add(scrollPaneMessagesToSend);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        panel.add(sendButton);
        return panel;
    }

    /**
     * This method is handler for changing user name from menu bar
     */
    private synchronized void setName() {
        userName = JOptionPane.showInputDialog(
                frame,
                "Enter your name:",
                "Messenger",
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * This method is handler for starting server from menu bar
     */
    private synchronized void startServer() {
        if (client != null) {
            client.stop();
        }
        if (server != null) {
            server.stop();
        }
        client = null;
        server = new MessengerServer(PORT, this);
        try {
            server.start();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Error while running server: ", exception);
        }
    }

    /**
     * This method is handler for starting client from menu bar
     */
    private synchronized void startClient() {
        if (client != null) {
            client.stop();
        }
        if (server != null) {
            server.stop();
        }
        server = null;

        String host = JOptionPane.showInputDialog(
                frame,
                "Enter host:",
                "Messenger",
                JOptionPane.PLAIN_MESSAGE);
        client = new MessengerClient(host, PORT, this);
        try {
            client.start();
        } catch (IOException exception) {
            logger.log(Level.WARNING, "Error while running client: ", exception);
        }
    }

    /**
     * This method sends current message to companion
     */
    private synchronized void sendMessage() {
        String message = messageToSendTextArea.getText();
        if (server != null) {
            try {
                server.sendMessage(userName, message);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(frame, "Unable to send message");
                logger.log(Level.WARNING, "Error while sending message from server: ", exception);
                return;
            }
        } else {
            if (client != null) {
                try {
                    client.sendMessage(userName, message);
                } catch (IOException exception) {
                    JOptionPane.showMessageDialog(frame, "Unable to send message");
                    logger.log(Level.WARNING, "Error while sending message from client: ", exception);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please, start server or client before messaging");
                logger.log(Level.WARNING, "Tried to send message while no client and server is running");
                return;
            }
        }
        messageToSendTextArea.setText("");
        messagesTextArea.append(userName + ":\n" + message + "\n");
    }

    /**
     * This method sends typing notification to companion
     */
    private void sendTypingNotification() {
        if (server != null) {
            try {
                server.sendTypingNotification(userName);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(frame, "Unable to send message");
                logger.log(Level.WARNING, "Error while sending message from server: ", exception);
            }
        } else {
            if (client != null) {
                try {
                    client.sendTypingNotification(userName);
                } catch (IOException exception) {
                    logger.log(Level.WARNING, "Error while sending typing notification: ", exception);
                }
            }
        }
    }

    /**
     * This method handle received messages,
     * server and client can call only this method of view
     */
    @Override
    public synchronized void receiveMessage(String senderName, String message) {
        messagesTextArea.append(senderName + ":\n" + message + "\n");
    }

    /**
     * This method handle typing for notification user about it
     */
    @Override
    public void receiveTypingNotification(String name) {
        lastCompanionName = name;
        lastTypingTime = System.currentTimeMillis();
    }
}
