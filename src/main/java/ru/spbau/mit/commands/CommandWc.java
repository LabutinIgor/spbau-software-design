package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class CommandWc extends Command {
    public CommandWc(List<String> args) {
        super(args);
    }

    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (getArgs().size() == 0) {
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
        } else {
            PrintWriter out = new PrintWriter(os);
            for (String arg : getArgs()) {
                Scanner in = new Scanner(new File(arg));
                int lines = 0, words = 0, bytes = 0;
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    lines++;
                    words += line.split(" ").length;
                    bytes += line.getBytes().length;
                }
                out.println(lines + " " + words + " " + bytes);
            }
            out.flush();
        }
    }
}
