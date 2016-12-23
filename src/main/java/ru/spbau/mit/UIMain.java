package ru.spbau.mit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class UIMain {
    private static final Object monitor = new Object();

    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private Direction lastDirection = Direction.STAY;

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
                    switch (c) {
                        case 'a':
                            lastDirection = Direction.LEFT;
                            monitor.notify();
                            break;
                        case 's':
                            lastDirection = Direction.DOWN;
                            monitor.notify();
                            break;
                        case 'd':
                            lastDirection = Direction.RIGHT;
                            monitor.notify();
                            break;
                        case 'w':
                            lastDirection = Direction.UP;
                            monitor.notify();
                            break;
                        default:
                            if (c >= '0' && c <= '9') {
                                //inventoryActionsQueue.add((int) c);
                            }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        frame.setSize(1000, 800);
        frame.setVisible(true);
    }

    public Direction getDirectionFromKeyboard() {
        synchronized (monitor) {
            while (lastDirection.equals(Direction.STAY)) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            Direction direction = lastDirection;
            lastDirection = Direction.STAY;
            return direction;
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

    private String getCharacteristicsText(List<Artifact> artifacts) {
        String text = "";
        for (int i = 0; i < artifacts.size(); i++) {
            Characteristics characteristics = artifacts.get(i).getCharacteristics();
            text += "ID: " + i + " FORCE: " + characteristics.getForce() + " ARMOR: " + characteristics.getArmor()
                    + " HP: " + characteristics.getLife() + "\n";
        }
        return text;
    }
}


