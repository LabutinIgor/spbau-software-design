package ru.spbau.mit;

public class Characteristics {
    private int force;
    private int armor;
    private int life;

    public Characteristics(int force, int armor, int life) {
        this.force = force;
        this.armor = armor;
        this.life = life;
    }

    public int getForce() {
        return force;
    }

    public int getArmor() {
        return armor;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
