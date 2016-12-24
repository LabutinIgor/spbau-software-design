package ru.spbau.mit.strategies;

import ru.spbau.mit.*;

/**
 * The Strategy interface is for strategy of moves of player
 */
public interface Strategy {
    /**
     * This method calculates direction of next move given by strategy
     */
    Direction getMoveDirection(Characteristics characteristics, Inventory inventory,
                               Position position, GameMap gameMap);

    /**
     * This method gets symbol for player with this strategy
     */
    char getSymbol();
}
