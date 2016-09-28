package ru.spbau.mit;

import java.io.IOException;
import java.util.*;

public class Lexer {
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
}
