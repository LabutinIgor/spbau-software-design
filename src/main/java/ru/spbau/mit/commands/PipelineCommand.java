package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

/**
 * The PipelineCommand class contains list of commands in one pipeline
 * and method run to execute all commands with corresponding input/output streams
 */
public class PipelineCommand implements Command {
    private List<Command> commands;

    /**
     * This constructor constructs class from list of consecutive commands of one pipeline
     */
    public PipelineCommand(List<Command> commands) {
        this.commands = commands;
    }

    /**
     * This method successively executes all commands with corresponding input/output streams
     */
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        InputStream currentInputStream = is;
        for (int i = 0; i < commands.size() - 1; i++) {
            PipedOutputStream currentOutputStream = new PipedOutputStream();
            InputStream nextInputStream = new PipedInputStream(currentOutputStream);

            commands.get(i).run(currentInputStream, currentOutputStream, environment);
            currentOutputStream.close();

            currentInputStream = nextInputStream;
        }
        commands.get(commands.size() - 1).run(currentInputStream, os, environment);
        os.flush();
    }
}
