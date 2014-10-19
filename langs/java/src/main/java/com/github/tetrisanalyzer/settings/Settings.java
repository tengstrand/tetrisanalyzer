package com.github.tetrisanalyzer.settings;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Settings implements Iterable<Setting> {
    private final String name;
    private final List<Setting> settings;

    public Settings(String name, Setting... settings) {
        this.name = name;
        this.settings = Arrays.asList(settings);
    }

    public String export() {
        String result = name + ":";

        for (Setting setting : settings) {
            result += "\n  " + setting.name + ": " + setting.value;
        }
        return result + "\n";
    }

    @Override
    public Iterator<Setting> iterator() {
        return settings.iterator();
    }

    @Override
    public String toString() {
        return "Settings{" +
                "name='" + name + '\'' +
                ", settings=" + settings +
                '}';
    }
}
