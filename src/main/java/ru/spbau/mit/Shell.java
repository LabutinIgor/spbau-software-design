package ru.spbau.mit;

import ru.spbau.mit.commands.Command;
import ru.spbau.mit.commands.PipelineCommand;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The Shell class provides command line interface
 */
public final class Shell {
    private static Logger logger = Logger.getLogger(Shell.class.getName());

    private Shell() {
    }

    /*
    This method launches shell
     */
    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
        } catch (IOException exception) {
            System.err.println("Could not setup logger configuration: " + exception.toString());
        }

        Scanner in = new Scanner(System.in);
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Environment environment = new Environment();
        String lastLine = in.nextLine();
        while (!lastLine.equals("exit")) {
            try {
                List<String> tokens = lexer.parseWords(lastLine);
                List<String> tokensAfterSubstitution = lexer.substituteVariables(tokens, environment);
                List<Command> commands = parser.parseCommands(tokensAfterSubstitution);
                PipelineCommand pipelineCommand = new PipelineCommand(commands);
                pipelineCommand.run(System.in, System.out, environment);
            } catch (IOException exception) {
                logger.log(Level.WARNING, "Exception in line '" + lastLine + "':", exception);
            }
            lastLine = in.nextLine();
        }
    }
}
