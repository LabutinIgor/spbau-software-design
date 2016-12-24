package ru.spbau.mit.game_objects;

/**
 * The Wall class represents wall object
 */
public class Wall implements GameObject {

    /**
     * This method gets symbol for this object
     */
    @Override
    public char getSymbol() {
        return '#';
    }
}
