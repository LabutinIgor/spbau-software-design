package ru.spbau.mit.commands;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import ru.spbau.mit.Environment;

import java.io.IOException;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class CommandAssignTest {

    @Test
    public void testRun() throws IOException {
        Environment environment = new Environment();

        CommandAssign commandAssign = new CommandAssign();
        new JCommander(commandAssign, "a", "x");
        commandAssign.run(System.in, System.out, environment);
        assertEquals("x", environment.getValue("a"));
    }
}
