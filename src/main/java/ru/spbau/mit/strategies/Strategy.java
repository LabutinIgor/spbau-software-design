package ru.spbau.mit.strategies;

import ru.spbau.mit.*;

public interface Strategy {
    Direction getMoveDirection(Characteristics characteristics, Inventory inventory,
                               Position position, GameMap gameMap);

    char getSymbol();
}
