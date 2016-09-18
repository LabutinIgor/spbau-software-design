package ru.spbau.mit;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, String> variables = new HashMap<>();

    public void assign(String name, String value) {
        variables.put(name, value);
    }

    public String getValue(String name) {
        return variables.get(name);
    }

}
