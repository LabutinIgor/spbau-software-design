package ru.spbau.mit;

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

    public Player(Strategy strategy, Characteristics characteristics, Position position) {
        this.strategy = strategy;
        this.characteristics = characteristics;
        this.position = position;
    }

    public void makeMove(GameMap gameMap) {
        Direction direction = strategy.getMoveDirection(characteristics, inventory, position, gameMap);
        Position newPosition = new Position(position, direction);
        if (newPosition.getX() < 0 || newPosition.getX() >= gameMap.getHeight() ||
                newPosition.getY() < 0 || newPosition.getY() >= gameMap.getWidth()) {
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
                        haveDamage(-artifact.getCharacteristics().getLife());
                    } else {
                        inventory.addArtifact(artifact);
                    }
                    moveToCell(newPosition, gameMap);
                }
            }
        }
    }

    public void haveDamage(int damage) {
        characteristics.setLife(characteristics.getLife() - Math.max(1, damage - characteristics.getArmor()));
    }

    private void moveToCell(Position newPosition, GameMap gameMap) {
        gameMap.setObject(newPosition, this);
        gameMap.setObject(position, new EmptyCell());
        position = newPosition;
    }

    public boolean isAlive() {
        return characteristics.getLife() > 0;
    }

    @Override
    public char getSymbol() {
        return strategy.getSymbol();
    }
}
