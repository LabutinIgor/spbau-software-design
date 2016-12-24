package ru.spbau.mit.commands;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import ru.spbau.mit.Environment;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static org.junit.Assert.*;

public class CommandCdTest {

    @Test
    public void testSimple() throws Exception {
        Command commandCd = new CommandCd();
        new JCommander(commandCd, "/tmp");
        PipedInputStream currentInputStream = new PipedInputStream();
        PipedOutputStream currentOutputStream = new PipedOutputStream();
        final Environment environment = new Environment();

        commandCd.run(currentInputStream, currentOutputStream, environment);
        currentOutputStream.close();

        assertEquals("/tmp", environment.getValue("$cwd"));

        Command commandCd2 = new CommandCd();
        new JCommander(commandCd2, "..");
        PipedInputStream currentInputStream2 = new PipedInputStream();
        PipedOutputStream currentOutputStream2 = new PipedOutputStream();

        commandCd2.run(currentInputStream2, currentOutputStream2, environment);
        currentOutputStream2.close();

        assertEquals("/", environment.getValue("$cwd"));
    }

}