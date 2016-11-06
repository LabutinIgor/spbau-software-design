package ru.spbau.mit.commands;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

public class CommandGrepTest {

    @Test
    public void testRun() throws IOException {
        Command commandGrep = new CommandGrep();
        new JCommander(commandGrep, "ab");
        PipedInputStream currentInputStream = new PipedInputStream();
        OutputStream inputForGrep = new PipedOutputStream(currentInputStream);
        PipedOutputStream currentOutputStream = new PipedOutputStream();
        InputStream nextInputStream = new PipedInputStream(currentOutputStream);

        PrintWriter inputForGrepWriter = new PrintWriter(inputForGrep);

        inputForGrepWriter.println("abcd");
        inputForGrepWriter.println("xyz");
        inputForGrepWriter.println("aaaaaab");
        inputForGrepWriter.close();

        commandGrep.run(currentInputStream, currentOutputStream, new Environment());
        currentOutputStream.close();

        Scanner in = new Scanner(nextInputStream);
        assertEquals("abcd", in.nextLine());
        assertEquals("aaaaaab", in.nextLine());
        assertFalse(in.hasNextLine());
    }

    @Test
    public void testRunWithCaseInsensitive() throws IOException {
        Command commandGrep = new CommandGrep();
        new JCommander(commandGrep, "aA.+b", "-i");
        PipedInputStream currentInputStream = new PipedInputStream();
        OutputStream inputForGrep = new PipedOutputStream(currentInputStream);
        PipedOutputStream currentOutputStream = new PipedOutputStream();
        InputStream nextInputStream = new PipedInputStream(currentOutputStream);

        PrintWriter inputForGrepWriter = new PrintWriter(inputForGrep);

        inputForGrepWriter.println("xxaab");
        inputForGrepWriter.println("aazzzb");
        inputForGrepWriter.println("xxxxx");
        inputForGrepWriter.println("aAb");
        inputForGrepWriter.println("AAAB");
        inputForGrepWriter.close();

        commandGrep.run(currentInputStream, currentOutputStream, new Environment());
        currentOutputStream.close();

        Scanner in = new Scanner(nextInputStream);
        assertEquals("aazzzb", in.nextLine());
        assertEquals("AAAB", in.nextLine());
        assertFalse(in.hasNextLine());
    }

    @Test
    public void testRunWithKeys() throws IOException {
        Command commandGrep = new CommandGrep();
        new JCommander(commandGrep, "aA.+b", "-A", "2", "-w");
        PipedInputStream currentInputStream = new PipedInputStream();
        OutputStream inputForGrep = new PipedOutputStream(currentInputStream);
        PipedOutputStream currentOutputStream = new PipedOutputStream();
        InputStream nextInputStream = new PipedInputStream(currentOutputStream);

        PrintWriter inputForGrepWriter = new PrintWriter(inputForGrep);

        inputForGrepWriter.println("aaab");
        inputForGrepWriter.println("aAaaab");
        inputForGrepWriter.println("xxxxx");
        inputForGrepWriter.println("xxxxx");
        inputForGrepWriter.println("aAb");
        inputForGrepWriter.println("aaab");

        inputForGrepWriter.close();

        commandGrep.run(currentInputStream, currentOutputStream, new Environment());
        currentOutputStream.close();

        Scanner in = new Scanner(nextInputStream);
        assertEquals("aAaaab", in.nextLine());
        assertEquals("xxxxx", in.nextLine());
        assertEquals("xxxxx", in.nextLine());
        assertFalse(in.hasNextLine());
    }
}
