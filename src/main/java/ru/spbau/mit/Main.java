package ru.spbau.mit;

import ru.spbau.mit.commands.Command;
import ru.spbau.mit.commands.PipelineCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        Parser parser = new Parser();
        Environment environment = new Environment();
        String lastLine = in.nextLine();
        while (!lastLine.equals("exit")) {
            List<String> tokens = parser.parseWords(lastLine);
            List<String> tokensAfterSubstitution = parser.substituteVariables(tokens, environment);
            List<Command> commands = parser.parseCommands(tokensAfterSubstitution);
            PipelineCommand pipelineCommand = new PipelineCommand(commands);
            pipelineCommand.run(new DataInputStream(System.in), new DataOutputStream(System.out), environment);
            //System.out.println();
            lastLine = in.nextLine();
        }
    }
}
