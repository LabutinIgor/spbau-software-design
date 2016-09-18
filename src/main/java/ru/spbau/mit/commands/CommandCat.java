package ru.spbau.mit.commands;

import org.apache.commons.io.IOUtils;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;

public class CommandCat extends Command {
    public CommandCat(List<String> args) {
        super(args);
    }

    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (getArgs().size() == 0) {
            IOUtils.copy(is, os);
            os.write('\n');
            os.flush();
        } else {
            for (String arg : getArgs()) {
                FileInputStream fileInputStream = new FileInputStream(new File(arg));
                IOUtils.copy(fileInputStream, os);
                os.write('\n');
                os.flush();
            }
        }
    }
}
