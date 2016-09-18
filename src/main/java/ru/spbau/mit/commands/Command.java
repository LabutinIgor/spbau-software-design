package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

public abstract class Command {
    private List<String> args;

    public Command(List<String> args) {
        this.args = args;
    }

    public abstract void run(InputStream is, OutputStream os, Environment environment)
            throws IOException;

    public List<String> getArgs() {
        return args;
    }
}
