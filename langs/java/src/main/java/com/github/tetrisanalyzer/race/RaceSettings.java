package com.github.tetrisanalyzer.race;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.settings.CustomSystemSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.SettingsReader;

import java.io.FileReader;
import java.util.Map;
import java.util.List;

public class RaceSettings {
    private final SettingsReader reader;

    public GameSettings gameRules;
    public BoardEvaluator boardEvaluator;
    public String parameterName;
    public int cellAreaRangeFrom;
    public int cellAreaRangeTo;

    public static RaceSettings fromString(String settings, CustomSystemSettings systemSettings) {
        try {
            return new RaceSettings((Map) new YamlReader(settings).read(), systemSettings);
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RaceSettings fromFile(String filename, CustomSystemSettings systemSettings) {
        try {
            return new RaceSettings((Map) new YamlReader(new FileReader(filename)).read(), systemSettings);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RaceSettings(Map settings, CustomSystemSettings systemSettings) {
        reader = new SettingsReader(settings, "race");

        String gameRulesId = reader.readString("game rules id");
        String boardEvaluatorId = reader.readString("board evaluator id");

        gameRules = systemSettings.findGameRules(gameRulesId);
        boardEvaluator = systemSettings.findBoardEvaluator(boardEvaluatorId);

        parameterName = reader.readString("parameter name");
        List<Integer> range = reader.readIntegers("cell area range", 2);
        cellAreaRangeFrom = range.get(0);
        cellAreaRangeTo = range.get(1);

        List<Map> games = reader.readMaps("games");
    }

    @Override
    public String toString() {
        return reader.toString();
    }
}
