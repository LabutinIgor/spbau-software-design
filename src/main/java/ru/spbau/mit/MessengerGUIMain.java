package ru.spbau.mit;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public final class MessengerGUIMain {
    private static final String HOST = "localhost";
    private static final int PORT = 8081;

    private static MessengerClient client = null;
    private static MessengerServer server = null;

    private JTextArea textArea;
    private JTextArea messageToSendTextArea;

    private MessengerGUIMain() {
    }

    public static void main(String[] args) {
        new MessengerGUIMain().start();
    }

    private void start() {
        JFrame frame = new JFrame("Messenger");
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

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setSize(250, 150);
        ScrollPane scrollPaneMessages = new ScrollPane();
        scrollPaneMessages.add(textArea);
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
        if (server != null) {
            server.stop();
        }
        server = new MessengerServer(PORT, this);
        try {
            server.receiveMessages();
        } catch (IOException e) {
            System.err.println("Error while running server");
        }
    }

    private synchronized void startClient() {
        if (client != null) {
            client.stop();
        }
        client = new MessengerClient(HOST, PORT, this);
        try {
            client.receiveMessages();
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
        textArea.append(message + "\n");
    }

    public synchronized void receiveMessage(String message) {
        textArea.append(message + "\n");
    }
}
