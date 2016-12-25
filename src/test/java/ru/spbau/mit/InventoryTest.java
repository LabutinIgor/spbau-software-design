package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.game_objects.Artifact;
import ru.spbau.mit.game_objects.ArtifactType;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

public class InventoryTest {
    @Test
    public void testAdd() {
        Inventory inventory = new Inventory();
        for (int i = 0; i < 20; i++) {
            Artifact sward = new Artifact(new Characteristics(10, 0, 0), ArtifactType.SWORD);
            Artifact armor = new Artifact(new Characteristics(10, 0, 0), ArtifactType.ARMOR);
            Artifact other = new Artifact(new Characteristics(10, 0, 0), ArtifactType.GENERAL);
            inventory.addArtifact(sward);
            inventory.addArtifact(armor);
            inventory.addArtifact(other);

            List<Artifact> artifacts = inventory.getArtifacts();
            assertSame(Math.min(3 * (i + 1), 9), artifacts.size());
            boolean hasSward = false;
            boolean hasArmor = false;
            boolean hasOther = false;
            for (Artifact artifact : artifacts) {
                if (artifact.isEnabled()) {
                    switch (artifact.getType()) {
                        case SWORD:
                            assertFalse(hasSward);
                            hasSward = true;
                            break;
                        case ARMOR:
                            assertFalse(hasArmor);
                            hasArmor = true;
                            break;
                        default:
                            assertFalse(hasOther);
                            hasOther = true;
                    }
                }
            }
        }
    }

    @Test
    public void testGetFreeId() {
        Inventory inventory = new Inventory();
        for (int i = 1; i <= 20; i++) {
            Artifact artifact = new Artifact(new Characteristics(10, 10, 10), ArtifactType.GENERAL);
            inventory.addArtifact(artifact);
            if (i <= 9) {
                assertSame(i, artifact.getId());
            }
        }
    }

    @Test
    public void testChangeArtifactState() {
        Inventory inventory = new Inventory();
        for (int i = 1; i <= 9; i++) {
            Artifact artifact = new Artifact(new Characteristics(10, 10, 10), ArtifactType.GENERAL);
            inventory.addArtifact(artifact);
        }
        List<Artifact> artifacts = inventory.getArtifacts();

        for (int i = 1; i < 9; i++) {
            for (Artifact artifact : artifacts) {
                assertSame(artifact.isEnabled(), artifact.getId() == i);
            }
            inventory.changeArtifactState(i + 1);
        }
    }

    @Test
    public void testApply() {
        Characteristics characteristics = new Characteristics(10, 10, 10);
        Inventory inventory = new Inventory();
        Characteristics newCharacteristics = inventory.apply(characteristics);
        assertSame(characteristics.getArmor(), newCharacteristics.getArmor());
        assertSame(characteristics.getForce(), newCharacteristics.getForce());
        assertSame(characteristics.getLife(), newCharacteristics.getLife());

        Artifact sward = new Artifact(new Characteristics(10, 0, 0), ArtifactType.SWORD);
        inventory.addArtifact(sward);
        newCharacteristics = inventory.apply(characteristics);
        Characteristics expectedCharacteristics = new Characteristics(20, 10, 10);
        assertSame(expectedCharacteristics.getArmor(), newCharacteristics.getArmor());
        assertSame(expectedCharacteristics.getForce(), newCharacteristics.getForce());
        assertSame(expectedCharacteristics.getLife(), newCharacteristics.getLife());

        Artifact armor = new Artifact(new Characteristics(0, 10, 0), ArtifactType.ARMOR);
        inventory.addArtifact(armor);
        newCharacteristics = inventory.apply(characteristics);
        expectedCharacteristics = new Characteristics(20, 20, 10);
        assertSame(expectedCharacteristics.getArmor(), newCharacteristics.getArmor());
        assertSame(expectedCharacteristics.getForce(), newCharacteristics.getForce());
        assertSame(expectedCharacteristics.getLife(), newCharacteristics.getLife());

        Artifact otherSward = new Artifact(new Characteristics(5, 0, 0), ArtifactType.SWORD);
        inventory.addArtifact(otherSward);
        assertSame(expectedCharacteristics.getArmor(), newCharacteristics.getArmor());
        assertSame(expectedCharacteristics.getForce(), newCharacteristics.getForce());
        assertSame(expectedCharacteristics.getLife(), newCharacteristics.getLife());
    }
}
