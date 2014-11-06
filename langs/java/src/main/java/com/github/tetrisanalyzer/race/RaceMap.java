package com.github.tetrisanalyzer.race;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.game.Duration;

import java.io.FileReader;
import java.util.Map;
import java.util.List;

public class RaceMap implements Race {
    private Map settings;

    private Duration duration;
    private int boardWidth;
    private int boardHeight;

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
        setBoardSize(settings, this);
    }

    private static void setBoardSize(Map settings, RaceMap raceMap) {
        if (!settings.containsKey("board")) {
            System.out.println("Missing 'board' attribute, set to default [10,20]");
            raceMap.boardWidth = 10;
            raceMap.boardHeight = 20;
            return;
        }
        Object b = settings.get("board");
        if (!(b instanceof List) || ((List)b).size() != 2) {
            throw new IllegalArgumentException("Attribute 'board' must be in the format: [width,height]");
        }
        List board = (List)b;
        raceMap.boardWidth = Integer.valueOf(board.get(0).toString());
        raceMap.boardHeight = Integer.valueOf(board.get(1).toString());
    }

    @Override
    public Duration duration() {
        return duration;
    }

    @Override
    public int boardWidth() {
        return boardWidth;
    }

    @Override
    public int boardHeight() {
        return boardHeight;
    }

    @Override
    public String toString() {
        return settings.toString();
    }
}
