package ru.spbau.mit.view;

import ru.spbau.mit.GameMap;
import ru.spbau.mit.game_objects.Player;

/**
 * The RoguelikeUI interface is interface for drawing ui and interaction with player by keyboard
 */
public interface RoguelikeUI {
    /**
     * This method draws ui
     */
    void draw(GameMap gameMap, Player player);

    /**
     * This method draws winning message
     */
    void drawWin();

    /**
     * This method draws loosing message
     */
    void drawLoose();

    /**
     * This method returns last pressed key
     */
    char getPressedKey();

    /**
     * This method waits until player pressed key to start new game
     */
    void waitUntilRestartPressed();
}
