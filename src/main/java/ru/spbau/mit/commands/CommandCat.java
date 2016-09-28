package ru.spbau.mit.commands;

import ru.spbau.mit.Environment;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class CommandCat extends Command {
    public CommandCat(List<String> args) {
        super(args);
    }

    @Override
    public void run(InputStream is, OutputStream os, Environment environment)
            throws IOException {
        if (getArgs().size() == 0) {
            Scanner in = new Scanner(is);
            PrintWriter out = new PrintWriter(os);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                out.println(line);
            }
            out.flush();
        } else {
            for (String arg : getArgs()) {
                Scanner in = new Scanner(new File(arg));
                PrintWriter out = new PrintWriter(os);
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    out.println(line);
                }
                out.flush();
            }
        }
    }
}
