package ru.spbau.mit;

import ru.spbau.mit.commands.*;

import java.io.IOException;
import java.util.*;

public class Parser {
    private int currentToken;
    private static final Set<Character> SYMBOLS = new HashSet<>(Arrays.asList(
            new Character[]{'\'', '"', '=', '$', '|'}
    ));

    public List<String> parseWords(String line) throws IOException {
        List<String> res = new ArrayList<>();
        String[] words = line.split(" ");
        for (String word : words) {
            String currentSubWord = "";
            for (int i = 0; i < word.length(); i++) {
                if (SYMBOLS.contains(word.charAt(i))) {
                    if (!currentSubWord.equals("")) {
                        res.add(currentSubWord);
                        currentSubWord = "";
                    }
                    res.add("" + word.charAt(i));
                } else {
                    currentSubWord += word.charAt(i);
                }
            }
            if (!currentSubWord.equals("")) {
                res.add(currentSubWord);
            }
        }
        return res;
    }

    public List<String> substituteVariables(List<String> tokens, Environment environment) throws IOException {
        List<String> res = new ArrayList<>();

        boolean inSingleQuotes = false;
        boolean isLastTokenSubstitution = false;
        for (String token : tokens) {
            if (token.equals("'")) {
                inSingleQuotes = !inSingleQuotes;
                res.add("'");
            } else {
                if (token.equals("$")) {
                    isLastTokenSubstitution = true;
                } else {
                    if (isLastTokenSubstitution) {
                        if (inSingleQuotes) {
                            res.add("$" + token);
                        } else {
                            res.addAll(parseWords(environment.getValue(token)));
                        }
                        isLastTokenSubstitution = false;
                    } else {
                        res.add(token);
                    }
                }
            }
        }

        return res;
    }

    private List<String> parseArguments(List<String> tokens) throws IOException {
        List<String> arguments = new ArrayList<>();
        List<String> currentArgument = new ArrayList<>();
        boolean inQuotes = false;
        while (!tokens.get(currentToken).equals("|")) {
            if (tokens.get(currentToken).equals("'") || tokens.get(currentToken).equals("\"")) {
                inQuotes = !inQuotes;
                if (!inQuotes) {
                    arguments.add(String.join(" ", currentArgument));
                    currentArgument.clear();
                }
            } else {
                if (inQuotes) {
                    currentArgument.add(tokens.get(currentToken));
                } else {
                    arguments.add(tokens.get(currentToken));
                }
            }

            currentToken++;
        }
        if (currentArgument.size() != 0) {
            arguments.add(String.join(" ", currentArgument));
        }
        return arguments;
    }

    private Command parseCommand(List<String> tokens) throws IOException {
        String commandName = tokens.get(currentToken++);

        if (tokens.get(currentToken).equals("=")) {
            currentToken++;
            List<String> args = parseArguments(tokens);
            args.add(0, commandName);
            return new CommandAssign(args);
        }

        List<String> args = parseArguments(tokens);
        switch (commandName) {
            case "cat":
                return new CommandCat(args);
            case "echo":
                return new CommandEcho(args);
            case "pwd":
                return new CommandPwd(args);
            case "wc":
                return new CommandWc(args);
            default:
                args.add(0, commandName);
                return new CommandExecute(args);
        }
    }

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
