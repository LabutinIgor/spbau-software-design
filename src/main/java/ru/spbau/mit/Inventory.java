package ru.spbau.mit;

import ru.spbau.mit.game_objects.Artifact;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Artifact> artifacts = new ArrayList<>();

    public void addArtifact(Artifact artifact) {
        artifact.setEnabled(true);
        artifact.setId(getFreeId());
        for (Artifact otherArtifact : artifacts) {
            if (otherArtifact.isEnabled() && artifact.getType() == otherArtifact.getType()) {
                artifact.setEnabled(false);
                break;
            }
        }
        artifacts.add(artifact);
    }

    private int getFreeId() {
        if (artifacts.size() < 9) {
            return artifacts.size() + 1;
        } else {
            for (Artifact artifact : artifacts) {
                if (!artifact.isEnabled()) {
                    artifacts.remove(artifact);
                    return artifact.getId();
                }
            }
        }
        return 0;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void changeArtifactState(int id) {
        Artifact artifactToChange = null;
        for (Artifact artifact : artifacts) {
            if (artifact.getId() == id) {
                artifactToChange = artifact;
                break;
            }
        }
        if (artifactToChange == null) {
            return;
        }

        if (!artifactToChange.isEnabled()) {
            for (Artifact otherArtifact : artifacts) {
                if (otherArtifact.getType() == artifactToChange.getType()) {
                    otherArtifact.setEnabled(false);
                }
            }
        }
        artifactToChange.setEnabled(!artifactToChange.isEnabled());
    }

    public Characteristics apply(Characteristics oldCharacteristics) {
        Characteristics resultCharacteristics = oldCharacteristics;
        for (Artifact artifact : artifacts) {
            resultCharacteristics = artifact.apply(resultCharacteristics);
        }
        return resultCharacteristics;
    }
}
