package ru.spbau.mit.commands;

import org.junit.Test;
import ru.spbau.mit.Environment;

import java.io.File;
import java.io.OutputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by YuryKravchenko on 31/12/2016.
 */
public class CommandLsTest {

    @Test
    public void testRun() throws Exception {
        CommandLs ls = new CommandLs();
        OutputStream out = new OutputStream() {
            private byte[] bytes = new byte[1000];
            private int size = 0;
            @Override
            public void write(int b) {
                bytes[size] = (byte)b;
                size++;
            }
            @Override
            public String toString() {
                return new String(Arrays.copyOf(bytes, size));
            }
        };
        Environment environment = new Environment();
        ls.run(null, out, environment);
        File curDir = new File(".");
        String res = "";
        for (File file: curDir.listFiles()) {
            res = res + file.getName() + "\n";
        }
        assertEquals(res, out.toString());
    }
}