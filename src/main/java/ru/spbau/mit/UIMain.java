package ru.spbau.mit;

import ru.spbau.mit.game_objects.Artifact;
import ru.spbau.mit.game_objects.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class UIMain {
    private static final Object monitor = new Object();

    private boolean restartPressed;
    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private char lastKey;

    UIMain() {
        frame = new JFrame();
        panel = new JPanel();
        textArea = new JTextArea();
        textArea.setSize(1000, 800);
        textArea.setEditable(false);
        textArea.setFont(new Font("monospaced", Font.PLAIN, 12));

        panel.add(textArea);
        frame.add(panel);

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                synchronized (monitor) {
                    char c = e.getKeyChar();
                    if (c == 'a' || c == 's' || c == 'w' || c == 'd' || (c >= '1' && c <= '9')) {
                        lastKey = c;
                        monitor.notify();
                    }
                    if (c == 'g') {
                        restartPressed = true;
                        monitor.notify();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public char getPressedKey() {
        synchronized (monitor) {
            lastKey = 0;
            while (lastKey == 0) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            return lastKey;
        }
    }

    public void waitUntilRestartPressed() {
        restartPressed = false;
        synchronized (monitor) {
            while (!restartPressed) {
                try {
                    monitor.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void draw(GameMap gameMap, Player player) {
        String text = "";
        for (int i = 0; i < gameMap.getHeight(); i++) {
            for (int j = 0; j < gameMap.getWidth(); j++) {
                text += gameMap.getObject(new Position(i, j)).getSymbol();
            }
            text += "\n";
        }
        text += "\n";
        Inventory inventory = player.getInventory();
        Characteristics currentCharacteristics = inventory.apply(player.getCharacteristics());
        text += "FORCE: " + currentCharacteristics.getForce() + "\n";
        text += "ARMOR: " + currentCharacteristics.getArmor() + "\n";
        text += "HP: " + currentCharacteristics.getLife() + "\n";

        text += "ARTIFACTS:\n";
        java.util.List<Artifact> swords = new ArrayList<>();
        java.util.List<Artifact> armors = new ArrayList<>();
        java.util.List<Artifact> others = new ArrayList<>();
        for (Artifact artifact : inventory.getArtifacts()) {
            switch (artifact.getType()) {
                case SWORD:
                    swords.add(artifact);
                    break;
                case ARMOR:
                    armors.add(artifact);
                    break;
                case GENERAL:
                    others.add(artifact);
            }
        }
        text += "SWORDS:\n";
        text += getCharacteristicsText(swords);
        text += "ARMOR:\n";
        text += getCharacteristicsText(armors);
        text += "OTHER:\n";
        text += getCharacteristicsText(others);

        textArea.setText(text);
    }

    public void drawWin() {
        String text = "You win :)\n";
        text += "Press g to start new game";
        textArea.setText(text);
    }

    public void drawLoose() {
        String text = "You loose :(\n";
        text += "Press g to start new game";
        textArea.setText(text);
    }

    private String getCharacteristicsText(List<Artifact> artifacts) {
        String text = "";
        for (Artifact artifact : artifacts) {
            Characteristics characteristics = artifact.getCharacteristics();
            text += "ID: " + artifact.getId() + " ACTIVE:" + artifact.isEnabled()
                    + " FORCE: " + characteristics.getForce() + " ARMOR: " + characteristics.getArmor()
                    + " HP: " + characteristics.getLife() + "\n";
        }
        return text;
    }
}
