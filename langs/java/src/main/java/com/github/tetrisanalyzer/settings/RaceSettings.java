package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.gui.Shortcuts;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceSettings {
    private final SettingsReader reader;

    public String filename;
    public GameSettings tetrisRules;
    public Map boardEvaluatorSettings;
    public Map pieceGeneratorSettings;
    public String parameterName;

    public ColoredBoard board;
    public String tetrisRulesId;
    public String pieceGeneratorId;
    public String boardEvaluatorId;

    public List<Color> colors;
    public String colorsString;
    public Shortcuts shortcuts;
    public List<RaceGameSettings> games = new ArrayList<>();

    /**
     * Used from tests.
     */
    public static RaceSettings fromString(String settings, SystemSettings systemSettings) {
        try {
            return new RaceSettings(null, (Map) new YamlReader(settings).read(), systemSettings);
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RaceSettings fromFile(String filename, SystemSettings systemSettings) {
        try {
            return new RaceSettings(filename, (Map) new YamlReader(new FileReader(filename)).read(), systemSettings);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RaceSettings(String filename, Map settings, SystemSettings systemSettings) {
        reader = new SettingsReader(settings, "race");

        this.filename = filename;
        Duration duration = reader.readDuration();
        board = reader.readBoard();

        tetrisRulesId = reader.readString("tetris rules id");
        pieceGeneratorId = reader.readString("piece generator id");
        boardEvaluatorId = reader.readString("board evaluator id");
        parameterName = reader.readString("parameter name");
        if (reader.exists("colors")) {
            colorsString = reader.readString("colors");
        }
        colors = reader.readColors("colors", defaultColors());
        shortcuts = reader.readShortcuts("windows");

        tetrisRules = systemSettings.findTetrisRules(tetrisRulesId);
        boardEvaluatorSettings = systemSettings.findBoardEvaluatorSettings(boardEvaluatorId);
        pieceGeneratorSettings = systemSettings.findPieceGeneratorSettings(pieceGeneratorId);

        List<Map> games = reader.readMaps("games");

        int idx = 0;
        for (Map gameMap : games) {
            Map evaluatorSettings = boardEvaluatorSettings(gameMap);
            Color color = colors.get(idx++ % colors.size());
            RaceGameSettings game = new RaceGameSettings(board, parameterName, gameMap, evaluatorSettings, pieceGeneratorSettings, duration, color);
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
     * Add the 'board' attribute to board evaluator settings (if exists).
     */
    private Map boardEvaluatorSettings(Map gameMap) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (gameMap.containsKey("board")) {
            result.put("board", gameMap.get("board"));
        }
        return result;
    }

    public String saveToFile(String windows) throws IOException {
        Files.write(Paths.get(filename), export(windows).getBytes("utf-8"));
        return filename;
    }

    public String export(String windows) {
        String colors = colorsString == null ? "" : "colors: " + colorsString + "\n";

        String games = "games:\n";

        for (RaceGameSettings game : this.games) {
            GameState state = game.gameState;
            games +=
                    " - parameter value: " + game.parameterValue + "\n" +
                    "   duration: " + game.duration + "\n" +
                    "   board: " + game.game.coloredBoard.export() + "\n" +
                    "   games: " + state.games + "\n" +
                    "   pieces: " + state.pieces + "\n" +
                    "   pieces total: " + state.totalPieces + "\n" +
                    "   rows: " + state.rows + "\n" +
                    "   min rows: " + state.minRows + "\n" +
                    "   max rows: " + state.maxRows + "\n" +
                    "   rows/game: " + state.rowsPerGame() + "\n" +
                    "   piece/s: " + state.piecesPerSecond() + "\n" +
                    "   piece generator state: " + state.pieceGenerator.export() + "\n" +
                    "   distribution: " + state.distribution.export() + "\n";
        }

        return "board: [" + board.width + "," + board.height + "]\n" +
                "tetris rules id: " + tetrisRulesId + "\n" +
                "piece generator id: " + pieceGeneratorId + "\n" +
                "board evaluator id: " + boardEvaluatorId + "\n" +
                "parameter name: " + parameterName + "\n" +
                colors +
                windows +
                games;
    }

    @Override
    public String toString() {
        return reader.toString();
    }
}
