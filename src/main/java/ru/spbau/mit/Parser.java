package ru.spbau.mit;

import com.beust.jcommander.JCommander;
import ru.spbau.mit.commands.*;

import java.io.IOException;
import java.util.*;

/**
 * The Parser class contains methods for parsing commands and arguments
 * from list of words and service symbols with substituted variables
 */
public class Parser {
    private int currentToken;

    /**
     * This method parses arguments of one command from list of tokens begins from currentToken
     */
    private List<String> parseArguments(List<String> tokens) throws IOException {
        List<String> arguments = new ArrayList<>();
        List<String> currentArgument = new ArrayList<>();
        boolean inQuotes = false;
        while (!tokens.get(currentToken).equals("|")) {
            if (tokens.get(currentToken).equals("'") || tokens.get(currentToken).equals("\"")) {
                inQuotes = !inQuotes;
                if (!inQuotes) {
                    arguments.add(String.join("", currentArgument));
                    currentArgument.clear();
                }
            } else {
                if (inQuotes) {
                    currentArgument.add(tokens.get(currentToken));
                } else {
                    if (!tokens.get(currentToken).equals(" ")) {
                        arguments.add(tokens.get(currentToken));
                    }
                }
            }
            currentToken++;
        }
        if (currentArgument.size() != 0) {
            arguments.add(String.join(" ", currentArgument));
        }
        return arguments;
    }

    /**
     * This method constructs command with given parameters using JCommander
     */
    private Command makeCommandWithArguments(Command command, List<String> args) {
        new JCommander(command, args.toArray(new String[0]));
        return command;
    }

    /**
     * This method parses one command from list of tokens begins from currentToken
     */
    private Command parseCommand(List<String> tokens) throws IOException {
        while (currentToken < tokens.size() - 1 && tokens.get(currentToken).equals(" ")) {
            currentToken++;
        }
        if (currentToken == tokens.size() - 1) {
            throw new IOException("Empty command");
        }
        String commandName = tokens.get(currentToken++);

        if (tokens.get(currentToken).equals("=")) {
            currentToken++;
            List<String> args = parseArguments(tokens);
            args.add(0, commandName);
            return makeCommandWithArguments(new CommandAssign(), args);
        }

        List<String> args = parseArguments(tokens);
        switch (commandName) {
            case "cat":
                return makeCommandWithArguments(new CommandCat(), args);
            case "echo":
                return makeCommandWithArguments(new CommandEcho(), args);
            case "pwd":
                return makeCommandWithArguments(new CommandPwd(), args);
            case "wc":
                return makeCommandWithArguments(new CommandWc(), args);
            case "grep":
                return makeCommandWithArguments(new CommandGrep(), args);
            default:
                args.add(0, commandName);
                return makeCommandWithArguments(new CommandExecute(), args);
        }
    }

    /**
     * This method parses all commands of one line from list of tokens
     * and returns list of parsed commands
     */
    public List<Command> parseCommands(List<String> tokens) throws IOException {
        List<Command> commands = new ArrayList<>();
        currentToken = 0;
        tokens.add("|");

        while (currentToken < tokens.size()) {
            Command command = parseCommand(tokens);
            if (!tokens.get(currentToken++).equals("|")) {
                throw new IOException("Invalid tokens");
            }
            commands.add(command);
        }

        return commands;
    }
}
