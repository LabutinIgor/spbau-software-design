package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YuryKravchenko on 29/12/2016.
 */
public class CommandCd implements Command {

    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Override
    public void run(InputStream is, OutputStream os, Environment environment) throws IOException {
        if (parameters.size() == 0) {
            environment.assign(Environment.CURRENT_DIR, System.getProperty("user.dir"));
            return;
        }
        if (parameters.get(0).equals("..")) {
            String parent = new File(environment.getValue(Environment.CURRENT_DIR)).getParent();
            if (parent != null) {
                environment.assign(Environment.CURRENT_DIR, parent);
            }
            return;
        }
        File dir = new File(environment.getValue(Environment.CURRENT_DIR) + "/" + parameters.get(0));
        if (dir.isDirectory()) {
            environment.assign(Environment.CURRENT_DIR,
                    environment.getValue(Environment.CURRENT_DIR) + "/" + parameters.get(0));
        } else {
            throw new IOException(parameters.get(0) + ": No such file or directory");
        }
    }
}
