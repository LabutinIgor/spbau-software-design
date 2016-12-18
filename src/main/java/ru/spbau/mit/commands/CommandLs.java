package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by n_buga on 02.12.16.
 * Print list of all file in the current directory.
 */
public class CommandLs implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment) throws IOException {
        String curDirPath = environment.getCurDir();
        File curDir = new File(curDirPath);
        File[] files = curDir.listFiles();
        PrintWriter out = new PrintWriter(os);
        for (File curFile: files) {
            if (curFile.isDirectory() || curFile.isFile()) {
                out.println(curFile.getName());
            }
        }
        out.flush();
    }
}
