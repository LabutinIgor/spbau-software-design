package ru.spbau.mit;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public final class MessengerGUIMain {
    private static final int PORT = 8081;

    private static MessengerClient client = null;
    private static MessengerServer server = null;

    private JFrame frame;
    private JTextArea messagesTextArea;
    private JTextArea messageToSendTextArea;

    private MessengerGUIMain() {
    }

    public static void main(String[] args) {
        new MessengerGUIMain().start();
    }

    private void start() {
        frame = new JFrame("Messenger");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JMenuBar menuBar = buildMenuBar();
        frame.setJMenuBar(menuBar);

        JPanel panel = buildPanel();
        frame.add(panel);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private JMenuBar buildMenuBar() {
        JMenu fileMenu = new JMenu("menu");

        JMenuItem downloadItem = new JMenuItem("start as server");
        downloadItem.addActionListener(e -> startServer());
        fileMenu.add(downloadItem);

        JMenuItem uploadItem = new JMenuItem("connect");
        uploadItem.addActionListener(e -> startClient());
        fileMenu.add(uploadItem);


        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        return menuBar;
    }

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

        JLabel labelNewMessage = new JLabel("New message:");

        panel.add(labelNewMessage);

        messageToSendTextArea = new JTextArea();
        messageToSendTextArea.setSize(200, 50);
        messageToSendTextArea.setBackground(Color.cyan);
        ScrollPane scrollPaneMessagesToSend = new ScrollPane();
        scrollPaneMessagesToSend.add(messageToSendTextArea);
        scrollPaneMessagesToSend.setSize(250, 60);

        panel.add(scrollPaneMessagesToSend);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        panel.add(sendButton);
        return panel;
    }

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
        } catch (IOException e) {
            System.err.println("Error while running server");
        }
    }

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
        } catch (IOException e) {
            System.err.println("Error while running server");
        }
    }

    private synchronized void sendMessage() {
        String message = messageToSendTextArea.getText();
        if (server == null) {
            try {
                client.sendMessage(message);
            } catch (IOException ignored) {
            }
        } else {
            try {
                server.sendMessage(message);
            } catch (IOException ignored) {
            }
        }
        messageToSendTextArea.setText("");
        messagesTextArea.append(message + "\n");
    }

    public synchronized void receiveMessage(String message) {
        messagesTextArea.append(message + "\n");
    }
}
