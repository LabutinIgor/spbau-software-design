package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * The CommandGrep class is command that finds matches of regex in
 * given files or input stream if no files given
 */
public class CommandGrep implements Command {
    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = "-i", description = "Is command case insensitive")
    private boolean caseInsensitive = false;

    @Parameter(names = "-w", description = "search only whole words")
    private boolean wholeWords = false;

    @Parameter(names = "-A", description = "number of printing lines after match")
    private Integer linesAfterMatch = 0;

    /**
     * This method executes command for one file or input stream
     */
    private void handleOneArgument(Pattern pattern, InputStream is, OutputStream os) throws IOException {
        Scanner in = new Scanner(is);
        PrintWriter out = new PrintWriter(os);

        int cntNextLinesToPrint = 0;
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (pattern.matcher(line).matches()) {
                cntNextLinesToPrint = Math.max(cntNextLinesToPrint, linesAfterMatch + 1);
            }
            if (cntNextLinesToPrint > 0) {
                out.println(line);
            }
            cntNextLinesToPrint = Math.max(0, cntNextLinesToPrint - 1);
        }

        out.flush();
    }

    /**
     * This method executes command
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (parameters.size() == 0) {
            throw new IOException("No parameters given in grep");
        }
        String regex;
        if (wholeWords) {
            regex = ".*\\b" + parameters.get(0) + "\\b.*";
        } else {
            regex = ".*" + parameters.get(0) + ".*";
        }
        Pattern pattern;
        if (caseInsensitive) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(regex);
        }

        if (parameters.size() == 1) {
            handleOneArgument(pattern, is, os);
        } else {
            for (String arg : parameters.subList(1, parameters.size())) {
                if (arg.charAt(0) == '/') {
                    /* user gave absolute path */
                    handleOneArgument(pattern, new FileInputStream(new File(arg)), os);
                } else {
                    /* relative path */
                    final String cwd = environment.getValue("$cwd");
                    handleOneArgument(pattern, new FileInputStream(new File(cwd + "/" + arg)), os);
                }
            }
        }
    }
}
