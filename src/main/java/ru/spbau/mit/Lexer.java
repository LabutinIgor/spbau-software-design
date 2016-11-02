package ru.spbau.mit;

import java.io.IOException;
import java.util.*;

/**
 * The Lexer class contains methods for division one line to
 * words and service symbols and then substituting variables
 */
public class Lexer {
    private static final Set<Character> SYMBOLS = new HashSet<>(Arrays.asList(
            new Character[]{'\'', '"', '=', '$', '|'}
    ));

    /**
     * This method divides line to list of words and service symbols
     */
    public List<String> parseWords(String line) {
        List<String> tokens = new ArrayList<>();
        String[] words = line.split(" ");
        for (String word : words) {
            String currentSubWord = "";
            for (int i = 0; i < word.length(); i++) {
                if (SYMBOLS.contains(word.charAt(i))) {
                    if (!currentSubWord.equals("")) {
                        tokens.add(currentSubWord);
                        currentSubWord = "";
                    }
                    tokens.add("" + word.charAt(i));
                } else {
                    currentSubWord += word.charAt(i);
                }
            }
            if (!currentSubWord.equals("")) {
                tokens.add(currentSubWord);
            }
        }
        return tokens;
    }

    /**
     * This method substitutes variables that are not in single quotes
     * and returns tokens after substitution
     */
    public List<String> substituteVariables(List<String> tokens, Environment environment) throws IOException {
        List<String> substitutedTokens = new ArrayList<>();

        boolean inSingleQuotes = false;
        boolean isLastTokenSubstitution = false;
        for (String token : tokens) {
            if (token.equals("'")) {
                inSingleQuotes = !inSingleQuotes;
                substitutedTokens.add("'");
            } else {
                if (token.equals("$")) {
                    isLastTokenSubstitution = true;
                } else {
                    if (isLastTokenSubstitution) {
                        if (inSingleQuotes) {
                            substitutedTokens.add("$" + token);
                        } else {
                            String value = environment.getValue(token);
                            if (value == null) {
                                throw new IOException("No such variable");
                            }
                            substitutedTokens.addAll(parseWords(value));
                        }
                        isLastTokenSubstitution = false;
                    } else {
                        substitutedTokens.add(token);
                    }
                }
            }
        }

        return substitutedTokens;
    }
}
