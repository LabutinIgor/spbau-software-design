package ru.spbau.mit.strategies;

import ru.spbau.mit.*;

/**
 * The HumanStrategy class represents strategy of human
 */
public class HumanStrategy implements Strategy {
    private Controller controller;

    public HumanStrategy(Controller controller) {
        this.controller = controller;
    }

    /**
     * This method calculates direction of next move that human made by keyboard,
     * it gets it from controller
     */
    @Override
    public Direction getMoveDirection(Characteristics characteristics, Inventory inventory,
                                      Position position, GameMap gameMap) {
        return controller.updateInventoryAndGetDirection();
    }

    /**
     * This method gets symbol for player with this strategy
     */
    @Override
    public char getSymbol() {
        return '@';
    }
}
