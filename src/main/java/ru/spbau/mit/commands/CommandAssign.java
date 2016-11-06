package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The CommandAssign class is command that assigns given value to given variable
 * Name of variable is first argument and value is second
 */
public class CommandAssign implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        environment.assign(parameters.get(0), parameters.get(1));
    }
}
