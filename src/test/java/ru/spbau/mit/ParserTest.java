package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.commands.Command;
import ru.spbau.mit.commands.CommandEcho;
import ru.spbau.mit.commands.CommandWc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ParserTest {

    @Test
    public void testParseCommands() throws IOException {
        List<String> tokens = new ArrayList<>(Arrays.asList("echo", " ", "a", " ", "'", "b", " ", "c", "'",
                "|", "wc"));
        List<Command> expectedCommands =
                Arrays.asList(new CommandEcho(Arrays.asList("a", "b c")), new CommandWc(Collections.emptyList()));
        Parser parser = new Parser();

        List<Command> commands = parser.parseCommands(tokens);
        assertSame(expectedCommands.size(), commands.size());
        for (int i = 0; i < expectedCommands.size(); i++) {
            Command expectedCommand = expectedCommands.get(i);
            Command command = commands.get(i);
            assertEquals(expectedCommand.getClass(), command.getClass());
            List<String> expectedArgs = expectedCommand.getArgs();
            List<String> args = command.getArgs();
            assertSame(expectedArgs.size(), args.size());
            for (int j = 0; j < expectedArgs.size(); j++) {
                assertEquals(expectedArgs.get(i), args.get(i));
            }
        }
    }
}

