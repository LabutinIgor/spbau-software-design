package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

/**
 * The Command class provides interface for command of shell
 * all commands should derive this class
 */
public abstract class Command {
    private List<String> args;

    /*
    This constructor constructs class from list of arguments of this command
     */
    public Command(List<String> args) {
        this.args = args;
    }

    /*
    This method executes command with given input/output streams and environment
     */
    public abstract void run(InputStream is, OutputStream os, Environment environment)
            throws IOException;

    /*
    This method returns arguments of this command
     */
    public List<String> getArgs() {
        return args;
    }
}
