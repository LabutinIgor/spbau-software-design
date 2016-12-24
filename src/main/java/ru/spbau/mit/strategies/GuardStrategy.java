package ru.spbau.mit.strategies;

import ru.spbau.mit.*;
import ru.spbau.mit.game_objects.Player;

public class GuardStrategy implements Strategy {
    @Override
    public Direction getMoveDirection(Characteristics characteristics, Inventory inventory,
                                      Position position, GameMap gameMap) {
        if (gameMap.getObject(new Position(position, Direction.LEFT)) instanceof Player) {
            return Direction.LEFT;
        }
        if (gameMap.getObject(new Position(position, Direction.RIGHT)) instanceof Player) {
            return Direction.RIGHT;
        }
        if (gameMap.getObject(new Position(position, Direction.UP)) instanceof Player) {
            return Direction.UP;
        }
        if (gameMap.getObject(new Position(position, Direction.DOWN)) instanceof Player) {
            return Direction.DOWN;
        }

        return Direction.STAY;
    }

    @Override
    public char getSymbol() {
        return 'B';
    }
}
