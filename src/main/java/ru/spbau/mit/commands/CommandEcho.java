package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

/**
 * The CommandEcho class is command that ignores input stream and prints all its arguments
 */
public class CommandEcho extends Command {
    public CommandEcho(List<String> args) {
        super(args);
    }

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        PrintWriter out = new PrintWriter(os);
        out.println(String.join(" ", getArgs()));
        out.flush();
    }
}
