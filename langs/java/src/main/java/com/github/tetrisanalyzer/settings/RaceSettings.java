package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaceSettings {
    private final SettingsReader reader;

    public GameSettings gameRules;
    public Map boardEvaluatorSettings;
    public PieceGenerator pieceGenerator;
    public String parameterName;
    public int cellAreaRangeFrom;
    public int cellAreaRangeTo;

    public List<RaceGameSettings> games = new ArrayList<>();

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

        Duration duration = reader.readDuration();
        ColoredBoard board = reader.readBoard();

        String gameRulesId = reader.readString("game rules id");
        String boardEvaluatorId = reader.readString("board evaluator id");
        String pieceGeneratorId = reader.readString("piece generator id");

        gameRules = systemSettings.findGameRules(gameRulesId);
        boardEvaluatorSettings = systemSettings.findBoardEvaluatoSettings(boardEvaluatorId);
        pieceGenerator = systemSettings.findPieceGenerator(pieceGeneratorId);

        parameterName = reader.readString("parameter name");
        List<Integer> range = reader.readIntegers("cell area range", 2);
        cellAreaRangeFrom = range.get(0);
        cellAreaRangeTo = range.get(1);

        List<Map> games = reader.readMaps("games");

        for (Map gameMap : games) {
            RaceGameSettings game = new RaceGameSettings(parameterName, gameMap, boardEvaluatorSettings, pieceGenerator, duration, board);
            this.games.add(game);
        }
    }

    @Override
    public String toString() {
        return reader.toString();
    }
}
