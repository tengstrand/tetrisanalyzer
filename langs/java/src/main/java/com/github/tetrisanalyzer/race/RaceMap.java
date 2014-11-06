package com.github.tetrisanalyzer.race;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.game.Duration;

import java.io.FileReader;
import java.util.Map;

public class RaceMap implements Race {
    private Map settings;

    private final Duration duration;

    public static RaceMap fromString(String string) {
        try {
            return new RaceMap((Map) new YamlReader(string).read());
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RaceMap fromFile(String filename) {
        try {
            return new RaceMap((Map) new YamlReader(new FileReader(filename)).read());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RaceMap(Map settings) {
        this.settings = settings;

        duration = new Duration((String)settings.get("duration"));
    }

    @Override
    public Duration duration() {
        return duration;
    }

    @Override
    public String toString() {
        return settings.toString();
    }
}
