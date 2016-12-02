package ru.spbau.mit.commands;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import ru.spbau.mit.Environment;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by n_buga on 02.12.16.
 */
public class CommandsLsCdTest {
    @Test
    public void testRun1() throws IOException {
        Path curPath = Paths.get(System.getProperty("user.dir"));
        Path tempDir = Files.createTempDirectory(curPath, "");
        try {
            Command commandLs = new CommandLs();
            Command commandCd = new CommandCd();
            Environment curEnv = new Environment();
            PipedOutputStream currentOutputStream = new PipedOutputStream();
            InputStream nextInputStream = new PipedInputStream(currentOutputStream);
            new JCommander(commandCd, tempDir.toString());
            commandCd.run(System.in, currentOutputStream, curEnv);
            assertEquals(tempDir.toAbsolutePath().toString(), curEnv.getCurDir());
            new JCommander(commandLs, "a bc");
            commandLs.run(nextInputStream, currentOutputStream, curEnv);
            currentOutputStream.close();
            Scanner in = new Scanner(nextInputStream);
            assertFalse(in.hasNextLine());
        } finally {
            Files.deleteIfExists(tempDir);
        }
    }
    @Test
    public void testRun2() throws IOException {
        Path curPath = Paths.get(System.getProperty("user.dir"));
        Path tempDir = Files.createTempDirectory(curPath, "");
        Path tempFile = Files.createTempFile(tempDir, "", "");
        try {
            Command commandLs = new CommandLs();
            Command commandCd = new CommandCd();
            Environment curEnv = new Environment();
            PipedOutputStream currentOutputStream = new PipedOutputStream();
            InputStream nextInputStream = new PipedInputStream(currentOutputStream);
            new JCommander(commandCd, tempDir.toString());
            commandCd.run(System.in, currentOutputStream, curEnv);
            assertEquals(tempDir.toAbsolutePath().toString(), curEnv.getCurDir());
            new JCommander(commandLs, "a bc");
            commandLs.run(nextInputStream, currentOutputStream, curEnv);
            currentOutputStream.close();
            Scanner in = new Scanner(nextInputStream);
            assertTrue(in.hasNextLine());
            assertEquals(in.nextLine(), tempFile.getFileName().toString());
        } finally {
            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(tempDir);
        }
    }
}
