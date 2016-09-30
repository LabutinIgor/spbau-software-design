package ru.spbau.mit;

import ru.spbau.mit.commands.Command;
import ru.spbau.mit.commands.PipelineCommand;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The Shell class provides command line interface
 */
public final class Shell {
    private Shell() {
    }

    /*
    This method launches shell
     */
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Environment environment = new Environment();
        String lastLine = in.nextLine();
        while (!lastLine.equals("exit")) {
            List<String> tokens = lexer.parseWords(lastLine);
            List<String> tokensAfterSubstitution = lexer.substituteVariables(tokens, environment);
            List<Command> commands = parser.parseCommands(tokensAfterSubstitution);
            PipelineCommand pipelineCommand = new PipelineCommand(commands);
            pipelineCommand.run(System.in, System.out, environment);
            lastLine = in.nextLine();
        }
    }
}
