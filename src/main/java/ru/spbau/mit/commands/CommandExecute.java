package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

/**
 * The CommandExecute class is command that executes external command with given in first argument name
 */
public class CommandExecute extends Command {
    public CommandExecute(List<String> args) {
        super(args);
    }

    /*
    This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment) throws IOException {
        Runtime.getRuntime().exec(getArgs().toArray(new String[getArgs().size()]));
    }
}
