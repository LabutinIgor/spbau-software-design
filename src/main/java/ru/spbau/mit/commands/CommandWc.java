package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The CommandWc class is command that prints number of lines, words and bytes
 * of given files or input stream if no files given
 */
public class CommandWc implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * This method executes command for one file or input stream
     */
    private void handleOneArgument(InputStream is, OutputStream os) throws IOException {
        Scanner in = new Scanner(is);
        PrintWriter out = new PrintWriter(os);
        int lines = 0, words = 0;
        int bytes = is.available();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            lines++;
            words += line.split(" ").length;
        }
        out.println(lines + " " + words + " " + bytes);
        out.flush();
    }

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (parameters.size() == 0) {
            handleOneArgument(is, os);
        } else {
            for (String arg : parameters) {
                handleOneArgument(new FileInputStream(
                        new File(environment.getValue(Environment.CURRENT_DIR) + "/" + arg)
                ), os);
            }
        }
    }
}
