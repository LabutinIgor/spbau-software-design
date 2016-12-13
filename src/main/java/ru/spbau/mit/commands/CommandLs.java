package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandLs implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    private boolean isFirstArg = true;

    /**
     * This method executes command for one file or input stream
     */
    private void handleOneArgument(File file, OutputStream os) throws IOException {
        final PrintWriter out = new PrintWriter(os);

        if (!isFirstArg) {
            out.println();
        }

        if (file.isFile()) {
            out.println(file.getName());
        } else if (file.isDirectory()) {
            if (parameters.size() > 1) {
                out.println(file.getName() + "/:");
            }
            final File[] dirFiles = file.listFiles();
            if (dirFiles == null) {
                throw new IOException("Could not read files in directory " + file.getAbsolutePath());
            }
            Arrays.stream(dirFiles).forEach(dirFile -> out.println(dirFile.getName()));
        } else {
            throw new IOException("file " + file.getAbsolutePath() + " neither a file nor a directory.");
        }

        isFirstArg = false;
        out.flush();
    }

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (parameters.size() == 0) {
            final String cwd = environment.getValue("$cwd");
            handleOneArgument(new File(cwd), os);
        } else {
            for (String arg : parameters) {
                if (arg.charAt(0) == '/') {
                    /* user gave absolute path */
                    handleOneArgument(new File(arg), os);
                } else {
                    /* relative path */
                    final String cwd = environment.getValue("$cwd");
                    handleOneArgument(new File(cwd + "/" + arg), os);
                }
            }
        }
    }
}
