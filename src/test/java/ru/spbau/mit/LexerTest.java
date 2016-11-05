package ru.spbau.mit;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class LexerTest {

    @Test
    public void testParseWords() {
        String line = "echo a $a 'a b' \"x\" | wc";
        List<String> expectedTokens = Arrays.asList(
                "echo", " ", "a", " ", "$", "a", " ", "'", "a",
                " ", "b", "'", " ", "\"", "x", "\"", " ", "|", " ", "wc");
        Lexer lexer = new Lexer();
        List<String> tokens = lexer.parseWords(line);

        assertSame(expectedTokens.size(), tokens.size());
        for (int i = 0; i < expectedTokens.size(); i++) {
            assertEquals(expectedTokens.get(i), tokens.get(i));
        }
    }

    @Test
    public void testSubstituteVariables() throws IOException {
        Environment environment = new Environment();
        environment.assign("a", "a");
        environment.assign("b", "b");
        environment.assign("c", "c");
        List<String> tokens = Arrays.asList("echo", " ", "$", "a", " ", "'", "$", "b", "'",
                " ", "\"", "$", "c", "\"");
        List<String> expectedSubstitutedTokens = Arrays.asList("echo", " ", "a", " ", "'", "$b", "'",
                " ", "\"", "c", "\"");
        Lexer lexer = new Lexer();
        List<String> substitutedTokens = lexer.substituteVariables(tokens, environment);

        assertSame(expectedSubstitutedTokens.size(), substitutedTokens.size());
        for (int i = 0; i < expectedSubstitutedTokens.size(); i++) {
            assertEquals(expectedSubstitutedTokens.get(i), substitutedTokens.get(i));
        }
    }
}
