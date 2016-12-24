package ru.spbau.mit.game_objects;

/**
 * The EmptyCell class represents empty cell
 */
public class EmptyCell implements GameObject {

    /**
     * This method gets symbol for this object
     */
    @Override
    public char getSymbol() {
        return '.';
    }
}
