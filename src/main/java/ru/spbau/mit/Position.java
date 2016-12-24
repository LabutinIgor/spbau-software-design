package ru.spbau.mit;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
