package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;

/**
 * The Command class provides interface for command of shell
 * all commands should derive this class
 */
public interface Command {

    /*
    This method executes command with given input/output streams and environment
     */
    void run(InputStream is, OutputStream os, Environment environment) throws IOException;

}
