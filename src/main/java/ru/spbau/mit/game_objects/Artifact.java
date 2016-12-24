package ru.spbau.mit.game_objects;

import ru.spbau.mit.Characteristics;

public class Artifact implements GameObject {
    private Characteristics characteristics;
    private ArtifactType artifactType;
    private boolean enabled = false;
    private int id;

    public Artifact(Characteristics characteristics, ArtifactType artifactType) {
        this.characteristics = characteristics;
        this.artifactType = artifactType;
    }

    public Characteristics apply(Characteristics oldCharacteristics) {
        if (enabled) {
            return new Characteristics(oldCharacteristics.getForce() + characteristics.getForce(),
                    oldCharacteristics.getArmor() + characteristics.getArmor(),
                    oldCharacteristics.getLife() + characteristics.getLife());
        } else {
            return oldCharacteristics;
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Characteristics getCharacteristics() {
        return characteristics;
    }

    public ArtifactType getType() {
        return artifactType;
    }

    @Override
    public char getSymbol() {
        return 'A';
    }
}
