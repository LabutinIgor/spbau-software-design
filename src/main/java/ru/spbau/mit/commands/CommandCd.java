package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by n_buga on 02.12.16.
 * Changes working directory.
 */
public class CommandCd implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment) throws IOException {
        File newDir;
        if (parameters.size() > 0) {
            newDir = new File(parameters.get(0));
        } else {
            Scanner scanner = new Scanner(is);
            newDir = new File(scanner.next());
        }
        if (!newDir.isDirectory()) {
            throw new NotImplementedException();
        }
        environment.setCurDir(newDir.getAbsolutePath());
    }
}
