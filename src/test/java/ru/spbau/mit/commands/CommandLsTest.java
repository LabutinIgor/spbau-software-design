package ru.spbau.mit.commands;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;

public class CommandLsTest {

    @Test
    public void testSimple() throws Exception {
        final File file1 = File.createTempFile("some_file", ".tmp");
        final File file2 = File.createTempFile("some_file_2", ".tmp");

        Command commandLs = new CommandLs();
        new JCommander(commandLs, file1.getParent());
        PipedInputStream currentInputStream = new PipedInputStream();
        PipedOutputStream currentOutputStream = new PipedOutputStream();
        InputStream nextInputStream = new PipedInputStream(currentOutputStream);

        commandLs.run(currentInputStream, currentOutputStream, new Environment());
        currentOutputStream.close();

        Scanner in = new Scanner(nextInputStream);
        assertTrue(checkContains(in, file1.getName(), file2.getName()));
    }

    private boolean checkContains(Scanner in, String ... strings) {
        final boolean[] was = new boolean[strings.length];
        for (int i = 0; i < was.length; i++) {
            was[i] = false;
        }

        while (in.hasNextLine()) {
            final String line = in.nextLine();
            for (int i = 0; i < strings.length; i++) {
                if (line.equals(strings[i])) {
                    was[i] = true;
                }
            }
        }

        for (int i = 0; i < was.length; i++) {
            if (!was[i]) {
                return false;
            }
        }

        return true;
    }

}