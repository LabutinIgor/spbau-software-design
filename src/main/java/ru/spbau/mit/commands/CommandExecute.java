package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The CommandExecute class is command that executes external command with given in first argument name
 */
public class CommandExecute implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment) throws IOException {
        Runtime.getRuntime().exec(parameters.toArray(new String[parameters.size()]), null, new File(environment.getValue(Environment.CURRENT_DIR)));
    }
}
