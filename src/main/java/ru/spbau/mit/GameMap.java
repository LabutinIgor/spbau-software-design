package ru.spbau.mit;

import ru.spbau.mit.game_objects.EmptyCell;
import ru.spbau.mit.game_objects.GameObject;

import java.util.Random;

public class GameMap {
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    GameObject objects[][];

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        objects = new GameObject[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                objects[i][j] = new EmptyCell();
            }
        }
    }

    public GameObject getObject(Position position) {
        return objects[position.getX()][position.getY()];
    }

    public void setObject(Position position, GameObject object) {
        objects[position.getX()][position.getY()] = object;
    }

    public Position getRandomFreePosition() {
        Random random = new Random();
        int x;
        int y;
        do {
            x = random.nextInt(height);
            y = random.nextInt(width);
        } while (!(objects[x][y] instanceof EmptyCell));

        return new Position(x, y);
    }
}
