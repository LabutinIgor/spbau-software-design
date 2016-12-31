package ru.spbau.mit.commands;

import com.beust.jcommander.Parameter;
import ru.spbau.mit.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * эта команда показывает имена файлов и папок в директории
 */
public class CommandLs implements Command {

    @Parameter
    private List<String> parameters = new ArrayList<>();

    /**
     * если нет параметров, то показываются файлы из текущей директории, а иначе из указанной директории
     */
    @Override
    public void run(InputStream is, OutputStream os, Environment environment) throws IOException {
        PrintWriter out = new PrintWriter(os);
        File dir;
        if (parameters.size() == 0) {
            dir = new File(environment.getValue(Environment.CURRENT_DIR));
        } else {
            dir = new File(environment.getValue(Environment.CURRENT_DIR) + "/" + parameters.get(0));
        }
        Arrays.stream(dir.listFiles()).forEach(file -> out.println(file.getName()));

        out.flush();
    }
}
