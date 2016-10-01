package ru.spbau.mit;


import org.junit.Test;

import java.io.IOException;

import static junitx.framework.Assert.assertEquals;
import static junitx.framework.Assert.fail;

public class EnvironmentTest {

    @Test
    public void testAssignNewVariableAndGet() {
        Environment environment = new Environment();
        environment.assign("a", "abc");
        environment.assign("b", "b");
        environment.assign("c", "x");
        try {
            assertEquals("abc", environment.getValue("a"));
            assertEquals("b", environment.getValue("b"));
            assertEquals("x", environment.getValue("c"));
        } catch (IOException exception) {
            fail("Unexpected exception: ", exception);
        }
    }

    @Test
    public void testReassignVariable() {
        Environment environment = new Environment();
        try {
            environment.assign("a", "abc");
            assertEquals("abc", environment.getValue("a"));
            assertEquals("abc", environment.getValue("a"));
            environment.assign("a", "b");
            assertEquals("b", environment.getValue("a"));
        } catch (IOException exception) {
            fail("Unexpected exception: ", exception);
        }
    }
}
