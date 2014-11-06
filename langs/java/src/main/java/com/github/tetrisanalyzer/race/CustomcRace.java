package com.github.tetrisanalyzer.race;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.game.Duration;

import java.io.FileReader;
import java.util.Map;
import java.util.List;

public class CustomcRace implements Race {
    private Map settings;

    private Duration duration;
    private int boardWidth;
    private int boardHeight;

    public static CustomcRace fromString(String settings) {
        try {
            return new CustomcRace((Map) new YamlReader(settings).read());
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static CustomcRace fromFile(String filename) {
        try {
            return new CustomcRace((Map) new YamlReader(new FileReader(filename)).read());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private CustomcRace(Map settings) {
        this.settings = settings;

        duration = new Duration((String)settings.get("duration"));
        setBoardSize(settings, this);
    }

    private static void setBoardSize(Map settings, CustomcRace customcRace) {
        if (!settings.containsKey("board")) {
            System.out.println("Missing 'board' attribute, set to default [10,20]");
            customcRace.boardWidth = 10;
            customcRace.boardHeight = 20;
            return;
        }
        Object objBoard = settings.get("board");
        if (!(objBoard instanceof List) || ((List)objBoard).size() != 2) {
            throw new IllegalArgumentException("Attribute 'board' must be in the format: [width,height]");
        }
        List board = (List)objBoard;
        customcRace.boardWidth = Integer.valueOf(board.get(0).toString());
        customcRace.boardHeight = Integer.valueOf(board.get(1).toString());
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
