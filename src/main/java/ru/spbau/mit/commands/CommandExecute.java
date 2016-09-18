package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

public class CommandExecute extends Command {

    public CommandExecute(List<String> args) {
        super(args);
    }

    @Override
    public void run(InputStream is, OutputStream os, Environment environment) throws IOException {
        Runtime.getRuntime().exec(getArgs().toArray(new String[getArgs().size()]));
    }
}
