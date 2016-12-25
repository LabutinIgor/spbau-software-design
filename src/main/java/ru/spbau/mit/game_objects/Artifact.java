package ru.spbau.mit.game_objects;

import ru.spbau.mit.Characteristics;

/**
 * The Artifact class represents artifact object,
 * it has characteristics which artifact adds to players characteristics,
 * each artifact has a type, at one time it can be no more than one active artifact of each type
 */
public class Artifact implements GameObject {
    private Characteristics characteristics;
    private ArtifactType artifactType;
    private boolean enabled = false;
    private int id;

    public Artifact(Characteristics characteristics, ArtifactType artifactType) {
        this.characteristics = characteristics;
        this.artifactType = artifactType;
    }

    /**
     * This method applies artifact to given characteristics
     */
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

    /**
     * This method gets symbol for this object
     */
    @Override
    public char getSymbol() {
        return 'A';
    }
}
