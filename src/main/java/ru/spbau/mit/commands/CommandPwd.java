package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The CommandPwd class is command that prints current directory
 */
public class CommandPwd implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        PrintWriter out = new PrintWriter(os);
        out.println(environment.getValue(Environment.CURRENT_DIR));
        out.flush();
    }
}
