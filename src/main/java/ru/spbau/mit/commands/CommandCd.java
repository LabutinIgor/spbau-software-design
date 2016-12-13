package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class CommandCd implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (parameters.size() != 1) {
            throw new IOException("cd accepts only 1 argument");
        }
        String arg = parameters.get(0);
        if (arg.charAt(0) != '/') {
            /* relative path */
            arg = environment.getValue("$cwd") + "/" + arg;
        }
        final File file = new File(arg);
        if (file.exists() && file.isDirectory()) {
            environment.assign("$cwd", file.getCanonicalPath());
        } else {
            throw new IOException("directory '" + arg + "' does not exist");
        }
    }
}
