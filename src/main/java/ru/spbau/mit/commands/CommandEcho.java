package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

public class CommandEcho extends Command {
    public CommandEcho(List<String> args) {
        super(args);
    }

    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        PrintWriter out = new PrintWriter(os);
        out.println(String.join(" ", getArgs()));
        out.flush();
    }
}
