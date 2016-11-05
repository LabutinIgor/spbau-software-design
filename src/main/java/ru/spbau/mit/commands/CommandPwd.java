package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

/**
 * The CommandPwd class is command that prints current directory
 */
public class CommandPwd extends Command {
    public CommandPwd(List<String> args) {
        super(args);
    }

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        PrintWriter out = new PrintWriter(os);
        out.println(System.getProperty("user.dir"));
        out.flush();
    }
}
