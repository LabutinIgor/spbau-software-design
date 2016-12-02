package ru.spbau.mit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Environment class maintain and changes variable values
 */
public class Environment {
    private Map<String, String> variables = new HashMap<>();
    /**
     * Save current work directory.
     */
    private String curDir = ".";

    /**
     * This method assigns value to variable
     * If there was no variable with given name, method creates it
     */
    public void assign(String name, String value) {
        variables.put(name, value);
    }

    /**
     * This method returns value of variable
     */
    public String getValue(String name) throws IOException {
        if (!variables.containsKey(name)) {
            throw new IOException("No such variable");
        }
        return variables.get(name);
    }

    /**
     * This method changes current work directory.
     */
    public void setCurDir(String newWorkingDir) {
        curDir = newWorkingDir;
    }

    /**
     * This method give a access to the current work directory.
     */

    public String getCurDir() {
        return curDir;
    }
}
