package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;

/**
 * Command is interface for command of shell
 * all commands should implements this interface
 */
public interface Command {

    /**
     * This method executes command with given input/output streams and environment
     */
    void run(InputStream is, OutputStream os, Environment environment) throws IOException;

}
