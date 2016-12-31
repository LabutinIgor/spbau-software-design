package ru.spbau.mit.commands;

import com.beust.jcommander.JCommander;
import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.Environment;

import static org.junit.Assert.*;

public class CommandCdTest {

    @Test
    public void testRun() throws Exception {
        CommandCd cd = new CommandCd();
        new JCommander(cd, "src/");
        Environment environment = new Environment();
        String cur = environment.getValue(Environment.CURRENT_DIR);
        cd.run(null, null, environment);
        assertEquals(cur + "/src/", environment.getValue(Environment.CURRENT_DIR));

        cd = new CommandCd();
        new JCommander(cd, "..");
        cd.run(null, null, environment);
        assertEquals(cur, environment.getValue(Environment.CURRENT_DIR));
    }
}