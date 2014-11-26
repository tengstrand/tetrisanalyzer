package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.game.Duration;

import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceSettings {
    private final SettingsReader reader;

    public GameSettings tetrisRules;
    public Map boardEvaluatorSettings;
    public Map pieceGeneratorSettings;
    public String parameterName;
    public int cellAreaRangeFrom;
    public int cellAreaRangeTo;

    public List<Color> colors;
    public List<RaceGameSettings> games = new ArrayList<>();

    public static RaceSettings fromString(String settings, SystemSettings systemSettings) {
        try {
            return new RaceSettings((Map) new YamlReader(settings).read(), systemSettings);
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RaceSettings fromFile(String filename, SystemSettings systemSettings) {
        try {
            return new RaceSettings((Map) new YamlReader(new FileReader(filename)).read(), systemSettings);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RaceSettings(Map settings, SystemSettings systemSettings) {
        reader = new SettingsReader(settings, "race");

        Duration duration = reader.readDuration();
        ColoredBoard board = reader.readBoard();

        String tetrisRulesId = reader.readString("tetris rules id");
        String boardEvaluatorId = reader.readString("board evaluator id");
        String pieceGeneratorId = reader.readString("piece generator id");

        tetrisRules = systemSettings.findTetrisRules(tetrisRulesId);
        boardEvaluatorSettings = systemSettings.findBoardEvaluatorSettings(boardEvaluatorId);
        pieceGeneratorSettings = systemSettings.findPieceGeneratorSettings(pieceGeneratorId);

        parameterName = reader.readString("parameter name");
        List<Integer> range = reader.readIntegers("cell area range", 2);
        cellAreaRangeFrom = range.get(0);
        cellAreaRangeTo = range.get(1);

        List<Map> games = reader.readMaps("games");

        colors = reader.readColors("colors", defaultColors());

        int idx = 0;
        for (Map gameMap : games) {
            Map evaluatorSettings = boardEvaluatorSettings(gameMap);
            Color color = colors.get(idx++ % colors.size());
            RaceGameSettings game = new RaceGameSettings(parameterName, gameMap, evaluatorSettings, pieceGeneratorSettings, duration, board, color);
            this.games.add(game);
        }
    }

    private List<Color> defaultColors() {
        return Arrays.asList(
                new Color(255,0,0),
                new Color(0,255,0),
                new Color(0,0,0),
                new Color(255,170,0),
                new Color(0,178,255),
                new Color(179,45,215),
                new Color(206,225,38),
                new Color(255,0,246),
                new Color(200,200,200)
        );
    }

    /**
     * Add the 'board' attribute to board evaluator settings.
     */
    private Map boardEvaluatorSettings(Map gameMap) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (gameMap.containsKey("board")) {
            result.put("board", gameMap.get("board"));
        }
        return result;
    }

    @Override
    public String toString() {
        return reader.toString();
    }
}
