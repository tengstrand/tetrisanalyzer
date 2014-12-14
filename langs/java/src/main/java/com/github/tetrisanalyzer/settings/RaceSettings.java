package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.gui.Shortcuts;
import com.github.tetrisanalyzer.gui.WindowLocation;

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
    public String parameterName;
    public boolean saveOnClose;

    public ColoredBoard board;
    public String tetrisRulesId;
    public String pieceGeneratorId;
    public String boardEvaluatorId;

    public List<Color> colors;
    public String colorsString;
    public Shortcuts shortcuts;
    public WindowLocation windowLocation;
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
        windowLocation = reader.readWindowLocation("window-location-size");
        shortcuts = reader.readShortcuts("zoom-windows");

        saveOnClose = reader.readBoolean("save-on-close", true);

        tetrisRules = systemSettings.findTetrisRules(tetrisRulesId);
        Map boardEvaluatorSettings = systemSettings.findBoardEvaluatorSettings(boardEvaluatorId);

        List<Map> games = reader.readMaps("games");

        int idx = 0;
        for (Map gameMap : games) {
            Map evaluatorSettings = boardEvaluatorSettings(boardEvaluatorSettings, gameMap);
            Color color = colors.get(idx++ % colors.size());
            RaceGameSettings game = new RaceGameSettings(systemSettings, board, parameterName, gameMap,
                    tetrisRulesId, pieceGeneratorId, boardEvaluatorId, evaluatorSettings, duration, color);
            if (game.color != color) {
                // Don't consume the global color if a color was explicitly specified.
                idx--;
            }
            this.games.add(game);
        }
    }

    private List<Color> defaultColors() {
        return Arrays.asList(
                ColorConverter.color("00b100"),
                ColorConverter.color("bc00ff"),
                ColorConverter.color("000000"),
                ColorConverter.color("005aff"),
                ColorConverter.color("ff0000"),
                ColorConverter.color("7debff"),
                ColorConverter.color("f78eff"),
                ColorConverter.color("b3ff00"),
                ColorConverter.color("ff7d00")
        );
    }

    /**
     * Add the 'board' attribute to board evaluator settings (if exists).
     */
    private Map boardEvaluatorSettings(Map boardEvaluatorSettings, Map gameMap) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (gameMap.containsKey("board")) {
            result.put("board", gameMap.get("board"));
        }
        return result;
    }

    public String saveToFile(WindowLocation windowLocation, String windows) throws IOException {
        Files.write(Paths.get(filename), export(windowLocation, windows).getBytes("utf-8"));
        return filename;
    }

    public String export(WindowLocation windowLocation, String windows) {
        String colors = colorsString == null ? "" : "colors: " + colorsString + "\n";

        String games = "games:\n";

        for (RaceGameSettings game : this.games) {
            GameState state = game.gameState;
            String tetrisRuleId = game.tetrisRulesId == this.tetrisRulesId ? "" : "   tetris rules id: " + game.tetrisRulesId + "\n";
            String pieceGeneratorId = game.pieceGeneratorId == this.pieceGeneratorId ? "" : "   piece generator id: " + game.pieceGeneratorId + "\n";
            String paused = game.permanentlyPaused ? "   paused: true\n" : "";
            String color = game.colorString == null ? "" : "   color: " + game.colorString + "\n";
            games +=
                    " - parameter value: " + game.parameterValue + "\n" +
                    tetrisRuleId +
                    pieceGeneratorId +
                    paused +
                    color +
                    "   duration: " + game.duration + "\n" +
                    "   board: " + game.game.coloredBoard.export() + "\n" +
                    "   games: " + state.games() + "\n" +
                    "   pieces: " + state.pieces + "\n" +
                    "   pieces total: " + state.totalPieces + "\n" +
                    "   rows: " + state.rows + "\n" +
                    "   rows total: " + state.totalRows + "\n" +
                    "   min rows: " + state.minRows() + "\n" +
                    "   max rows: " + state.maxRows() + "\n" +
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
                "save-on-close: " + saveOnClose + "\n" +
                colors +
                windowLocation.export() + "\n" +
                windows +
                games;
    }

    @Override
    public String toString() {
        return reader.toString();
    }
}
