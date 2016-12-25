package ru.spbau.mit.game_objects;

import org.junit.Test;
import ru.spbau.mit.Characteristics;

import static junitx.framework.Assert.assertSame;

public class ArtifactTest {

    @Test
    public void testApply() {
        Characteristics characteristics = new Characteristics(10, 10, 10);
        Artifact artifact = new Artifact(new Characteristics(1, 2, 3), ArtifactType.GENERAL);

        Characteristics newCharacteristics = artifact.apply(characteristics);

        assertSame(characteristics.getArmor(), newCharacteristics.getArmor());
        assertSame(characteristics.getForce(), newCharacteristics.getForce());
        assertSame(characteristics.getLife(), newCharacteristics.getLife());

        artifact.setEnabled(true);
        Characteristics expectedCharacteristics = new Characteristics(11, 12, 13);
        newCharacteristics = artifact.apply(characteristics);
        assertSame(expectedCharacteristics.getArmor(), newCharacteristics.getArmor());
        assertSame(expectedCharacteristics.getForce(), newCharacteristics.getForce());
        assertSame(expectedCharacteristics.getLife(), newCharacteristics.getLife());
    }
}
