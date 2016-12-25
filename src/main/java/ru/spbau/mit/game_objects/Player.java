package ru.spbau.mit.game_objects;

import ru.spbau.mit.*;
import ru.spbau.mit.strategies.Strategy;

/**
 * The Player class represents player,
 * it has characteristics, inventory and
 * can make move with some strategy
 */
public class Player implements GameObject {
    private Strategy strategy;
    private Characteristics characteristics;
    private Inventory inventory = new Inventory();
    private Position position;

    public Characteristics getCharacteristics() {
        return characteristics;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * This constructor initializes all fields needed for player
     */
    public Player(Strategy strategy, Characteristics characteristics, Position position) {
        this.strategy = strategy;
        this.characteristics = characteristics;
        this.position = position;
    }

    /**
     * This method makes one move using strategy
     */
    public void makeMove(GameMap gameMap) {
        Direction direction = strategy.getMoveDirection(characteristics, inventory, position, gameMap);
        if (direction == Direction.STAY) {
            return;
        }
        Position newPosition = new Position(position, direction);
        if (newPosition.getX() < 0 || newPosition.getX() >= gameMap.getHeight()
                || newPosition.getY() < 0 || newPosition.getY() >= gameMap.getWidth()) {
            return;
        }

        GameObject gameObject = gameMap.getObject(newPosition);
        if (gameObject instanceof EmptyCell) {
            moveToCell(newPosition, gameMap);
        } else {
            if (gameObject instanceof Player) {
                Player enemy = ((Player) gameObject);
                enemy.haveDamage(inventory.apply(characteristics).getForce());
                if (!enemy.isAlive()) {
                    gameMap.setObject(newPosition, new EmptyCell());
                }
            } else {
                if (gameObject instanceof Artifact) {
                    Artifact artifact = (Artifact) gameObject;
                    if (artifact.getType().equals(ArtifactType.MEDICINE)) {
                        cure(artifact.getCharacteristics().getLife());
                    } else {
                        inventory.addArtifact(artifact);
                    }
                    moveToCell(newPosition, gameMap);
                }
            }
        }
    }

    /**
     * This method makes damage from hit of given force
     */
    private void haveDamage(int force) {
        Characteristics characteristicsWithInventory = inventory.apply(characteristics);
        characteristics.setLife(characteristics.getLife()
                - Math.max(1, force - characteristicsWithInventory.getArmor()));
    }

    /**
     * This method add given number to life of player
     */
    private void cure(int life) {
        characteristics.setLife(characteristics.getLife() + life);
    }

    /**
     * This method moves player to given position
     */
    private void moveToCell(Position newPosition, GameMap gameMap) {
        gameMap.setObject(newPosition, this);
        gameMap.setObject(position, new EmptyCell());
        position = newPosition;
    }

    /**
     * This method returns is player alive
     */
    public boolean isAlive() {
        return inventory.apply(characteristics).getLife() > 0;
    }

    /**
     * This method gets symbol for player
     */
    @Override
    public char getSymbol() {
        return strategy.getSymbol();
    }
}
