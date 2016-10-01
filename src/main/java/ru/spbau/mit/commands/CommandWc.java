package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * The CommandWc class is command that prints number of lines, words and bytes
 * of given files or input stream if no files given
 */
public class CommandWc extends Command {
    public CommandWc(List<String> args) {
        super(args);
    }

    /*
    This method executes command for one file or input stream
     */
    private void handleOneArgument(InputStream is, OutputStream os) {
        Scanner in = new Scanner(is);
        int lines = 0, words = 0, bytes = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            lines++;
            words += line.split(" ").length;
            bytes += line.getBytes().length;
        }
        PrintWriter out = new PrintWriter(os);
        out.println(lines + " " + words + " " + bytes);
        out.flush();
    }

    /*
    This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (getArgs().size() == 0) {
            handleOneArgument(is, os);
        } else {
            for (String arg : getArgs()) {
                handleOneArgument(new FileInputStream(new File(arg)), os);
            }
        }
    }
}
