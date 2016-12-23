package ru.spbau.mit;

public class HumanStrategy implements Strategy {
    private Controller controller;

    HumanStrategy(Controller controller) {
        this.controller = controller;
    }

    @Override
    public Direction getMoveDirection(Characteristics characteristics, Inventory inventory,
                                      Position position, GameMap gameMap) {
        return controller.getDirectionForHuman();
    }

    @Override
    public char getSymbol() {
        return '@';
    }
}
