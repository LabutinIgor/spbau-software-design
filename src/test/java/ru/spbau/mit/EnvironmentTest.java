package ru.spbau.mit;


import org.junit.Test;

import static junitx.framework.Assert.assertEquals;

public class EnvironmentTest {

    @Test
    public void testAssignNewVariableAndGet() {
        Environment environment = new Environment();
        environment.assign("a", "abc");
        environment.assign("b", "b");
        environment.assign("c", "x");
        assertEquals("abc", environment.getValue("a"));
        assertEquals("b", environment.getValue("b"));
        assertEquals("x", environment.getValue("c"));
    }

    @Test
    public void testReassignVariable() {
        Environment environment = new Environment();
        environment.assign("a", "abc");
        assertEquals("abc", environment.getValue("a"));
        assertEquals("abc", environment.getValue("a"));
        environment.assign("a", "b");
        assertEquals("b", environment.getValue("a"));
    }
}
