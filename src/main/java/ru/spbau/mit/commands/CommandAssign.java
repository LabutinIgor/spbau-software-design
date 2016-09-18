package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

public class CommandAssign extends Command {
    public CommandAssign(List<String> args) {
        super(args);
    }

    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        environment.assign(getArgs().get(0), getArgs().get(1));
    }
}
