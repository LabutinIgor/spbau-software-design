package ru.spbau.mit;

public class Artifact implements GameObject {
    private Characteristics characteristics;
    private ArtifactType artifactType;
    private boolean enabled = false;

    Artifact(Characteristics characteristics, ArtifactType artifactType) {
        this.characteristics = characteristics;
        this.artifactType = artifactType;
    }

    Characteristics apply(Characteristics oldCharacteristics) {
        if (enabled) {
            return new Characteristics(oldCharacteristics.getForce() + characteristics.getForce(),
                    oldCharacteristics.getArmor() + characteristics.getArmor(),
                    oldCharacteristics.getLife() + characteristics.getLife());
        } else {
            return oldCharacteristics;
        }
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    boolean isEnabled() {
        return enabled;
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
