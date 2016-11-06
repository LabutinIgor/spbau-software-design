package ru.spbau.mit;

import com.beust.jcommander.JCommander;
import org.junit.Test;
import ru.spbau.mit.commands.Command;
import ru.spbau.mit.commands.CommandEcho;
import ru.spbau.mit.commands.CommandWc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ParserTest {

    @Test
    public void testParseCommands() throws IOException {
        List<String> tokens = new ArrayList<>(Arrays.asList("echo", " ", "a", " ", "'", "b", " ", "c", "'",
                "|", "wc"));
        Command commandEcho = new CommandEcho();
        new JCommander(commandEcho, "a", "b c");
        Command commandWc = new CommandWc();
        new JCommander(commandWc);
        List<Command> expectedCommands =
                Arrays.asList(commandEcho, commandWc);
        Parser parser = new Parser();
        List<Command> commands = parser.parseCommands(tokens);
        assertSame(expectedCommands.size(), commands.size());
        for (int i = 0; i < expectedCommands.size(); i++) {
            Command expectedCommand = expectedCommands.get(i);
            Command command = commands.get(i);
            assertEquals(expectedCommand.getClass(), command.getClass());
        }
    }
}
