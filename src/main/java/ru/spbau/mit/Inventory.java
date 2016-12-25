package ru.spbau.mit;

import ru.spbau.mit.game_objects.Artifact;

import java.util.ArrayList;
import java.util.List;

/**
 * The Inventory class represents players inventory with artifacts,
 * it can add artifact, change its state
 * and calculate characteristics with inventory applied
 */
public class Inventory {
    private List<Artifact> artifacts = new ArrayList<>();

    /**
     * This method adds artifact to inventory
     */
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

    /**
     * This method gets id number from 1 to 9 for new artifact,
     * if there are 9 artifacts then it removes some inactive artifact and returns its id
     */
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

    /**
     * This method changes state of artifact,
     * if given artifact activates, current active artifact of this type deactivates
     */
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

    /**
     * This method calculates characteristics of player with this inventory
     * by given initial characteristics
     */
    public Characteristics apply(Characteristics oldCharacteristics) {
        Characteristics resultCharacteristics = oldCharacteristics;
        for (Artifact artifact : artifacts) {
            resultCharacteristics = artifact.apply(resultCharacteristics);
        }
        return resultCharacteristics;
    }
}
