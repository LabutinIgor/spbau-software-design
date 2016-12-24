package ru.spbau.mit.strategies;

import ru.spbau.mit.*;

import java.util.Random;

public class BotRandomStrategy implements Strategy {
    @Override
    public Direction getMoveDirection(Characteristics characteristics, Inventory inventory,
                                      Position position, GameMap gameMap) {
        int direction = new Random().nextInt(4);
        switch (direction) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.LEFT;
            default:
                return Direction.RIGHT;
        }
    }

    @Override
    public char getSymbol() {
        return 'B';
    }
}
