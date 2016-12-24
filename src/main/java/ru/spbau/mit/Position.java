package ru.spbau.mit;

/**
 * The Position class represents position of object in map
 */
public class Position {
    private int x;
    private int y;

    /**
     * This constructor initializes position with given coordinates
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * This constructor initializes position as
     * one move from given position on given direction
     */
    public Position(Position previousPosition, Direction direction) {
        x = previousPosition.getX();
        y = previousPosition.getY();
        switch (direction) {
            case UP:
                x--;
                break;
            case DOWN:
                x++;
                break;
            case LEFT:
                y--;
                break;
            case RIGHT:
                y++;
                break;
            default:
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
