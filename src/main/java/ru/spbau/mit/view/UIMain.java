package ru.spbau.mit.view;

import ru.spbau.mit.Characteristics;
import ru.spbau.mit.GameMap;
import ru.spbau.mit.Inventory;
import ru.spbau.mit.Position;
import ru.spbau.mit.game_objects.Artifact;
import ru.spbau.mit.game_objects.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

/**
 * The UIMain class is class for drawing ui and interaction with player by keyboard
 */
public class UIMain implements RoguelikeUI {
    private static final Object MONITOR = new Object();

    private boolean restartPressed;
    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private char lastKey;

    /**
     * This constructor initializes frame and sets up key listener
     */
    public UIMain() {
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
                synchronized (MONITOR) {
                    char c = e.getKeyChar();
                    if (c == 'a' || c == 's' || c == 'w' || c == 'd' || (c >= '1' && c <= '9')) {
                        lastKey = c;
                        MONITOR.notify();
                    }
                    if (c == 'g') {
                        restartPressed = true;
                        MONITOR.notify();
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

    /**
     * This method returns last pressed key
     */
    @Override
    public char getPressedKey() {
        synchronized (MONITOR) {
            lastKey = 0;
            while (lastKey == 0) {
                try {
                    MONITOR.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            return lastKey;
        }
    }

    /**
     * This method waits until player pressed key to start new game
     */
    @Override
    public void waitUntilRestartPressed() {
        restartPressed = false;
        synchronized (MONITOR) {
            while (!restartPressed) {
                try {
                    MONITOR.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    /**
     * This method draws ui
     */
    @Override
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
                    break;
                default:
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

    /**
     * This method draws winning message
     */
    @Override
    public void drawWin() {
        String text = "You win :)\n";
        text += "Press g to start new game";
        textArea.setText(text);
    }

    /**
     * This method draws loosing message
     */
    @Override
    public void drawLoose() {
        String text = "You loose :(\n";
        text += "Press g to start new game";
        textArea.setText(text);
    }

    /**
     * This method returns text describing given artifacts
     */
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
