package ru.spbau.mit.commands;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import ru.spbau.mit.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CommandEchoTest {

    @Test
    public void testRun() {
        Command commandEcho = new CommandEcho();
        new JCommander(commandEcho, "a", "b c");
        PipedOutputStream currentOutputStream = new PipedOutputStream();
        InputStream nextInputStream = new PipedInputStream(currentOutputStream);
        commandEcho.run(System.in, currentOutputStream, new Environment());
        currentOutputStream.close();
        Scanner in = new Scanner(nextInputStream);
        assertTrue(in.hasNextLine());
        String expectedOutput = "a b c";
        assertEquals(expectedOutput, in.nextLine());
        assertFalse(in.hasNextLine());
    }
}
