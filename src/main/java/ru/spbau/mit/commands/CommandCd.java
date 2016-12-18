package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.nio.file.*;
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
        String newDirString;
        if (parameters.size() > 0) {
            newDirString = parameters.get(0);
        } else {
            Scanner scanner = new Scanner(is);
            newDirString = scanner.next();
        }
        if (newDirString.equals("..")) {
            newDirString = Paths.get(environment.getCurDir()).getParent().toString();
        } else if (newDirString.charAt(0) == '.') {
            newDirString = environment.getCurDir() + newDirString.substring(1);
        } else if (newDirString.charAt(0) != File.separatorChar) {
            newDirString = Paths.get(environment.getCurDir(), newDirString).toString();
        }
        newDir = new File(newDirString);
        if (!newDir.exists()) {
            throw new IOException("The directory doesn't exist");
        }
        if (!newDir.isDirectory()) {
            throw  new IOException("It is file, not directory");
        }
        environment.setCurDir(newDir.getAbsolutePath());
    }
}
