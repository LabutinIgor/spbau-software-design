package ru.spbau.mit;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Artifact> artifacts = new ArrayList<>();

    public void addArtifact(Artifact artifact) {
        artifact.setEnabled(true);
        artifacts.add(artifact);
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void changeArtifactState(int index) {
        Artifact artifact = artifacts.get(index);
        if (!artifact.isEnabled()) {
            for (Artifact otherArtifact : artifacts) {
                if (otherArtifact.getType() == artifact.getType()) {
                    otherArtifact.setEnabled(false);
                }
            }
        }
        artifact.setEnabled(!artifact.isEnabled());
    }

    public Characteristics apply(Characteristics oldCharacteristics) {
        Characteristics resultCharacteristics = oldCharacteristics;
        for (Artifact artifact : artifacts) {
            resultCharacteristics = artifact.apply(resultCharacteristics);
        }
        return resultCharacteristics;
    }
}
