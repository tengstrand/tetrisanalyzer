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
import java.util.*;
import java.util.List;

import static java.util.Map.Entry;

public class RaceSettings {
    private final SettingsReader reader;

    public String filename;
    public int level;
    public int numberOfKnownPieces;
    public int masterDepth;
    public GameSettings tetrisRules;
    public String parameterName;
    public Map parameters;
    public boolean saveOnClose;
    public boolean restartOnFileChange;

    public double areaPercentage;
    public ColoredBoard startBoard;
    public String startBoardText;
    public String tetrisRulesId;
    public String pieceGeneratorId;
    public String boardEvaluatorId;

    public List<Color> colors;
    public String colorsString;
    public Shortcuts shortcuts;
    public WindowLocation windowLocation;
    public RaceGamesSettings games = new RaceGamesSettings();

    /**
     * Used from tests.
     */
    public static RaceSettings fromString(String settings, SystemSettings systemSettings, boolean showAll) {
        try {
            return new RaceSettings(null, (Map) new YamlReader(settings).read(), systemSettings, showAll);
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static RaceSettings fromFile(String filename, SystemSettings systemSettings, boolean showAll) {
        try {
            return new RaceSettings(filename, (Map) new YamlReader(new FileReader(filename)).read(), systemSettings, showAll);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RaceSettings(String filename, Map settings, SystemSettings systemSettings, boolean showAll) {
        reader = new SettingsReader(settings, "race");

        this.filename = filename;
        level = reader.readInteger("level", 1);
        numberOfKnownPieces = reader.readInteger("number of known pieces", 1);
        masterDepth = reader.readInteger("master depth", 0);
        Duration duration = reader.readDuration();
        areaPercentage = reader.readDouble("area %", 30);
        startBoard = reader.readBoard("start board", null);
        startBoardText = reader.readString("start board", null);

        tetrisRulesId = reader.readString("tetris rules id");
        pieceGeneratorId = reader.readString("piece generator id");
        boardEvaluatorId = reader.readString("board evaluator id");
        parameterName = reader.readString("parameter name", "");
        parameters = reader.readMap("parameters", null);

        if (reader.exists("colors")) {
            colorsString = reader.readString("colors");
        }
        colors = reader.readColors("colors", defaultColors());
        windowLocation = reader.readWindowLocation("window location size");
        shortcuts = reader.readShortcuts("zoom windows");

        saveOnClose = reader.readBoolean("save on close", true);
        restartOnFileChange = reader.readBoolean("restart on file change", false);

        tetrisRules = systemSettings.findTetrisRules(tetrisRulesId);

        List<Map> games = reader.readMaps("games");

        int idx = 0;
        for (Map gameMap : games) {
            String evaluatorId = evaluatorId(boardEvaluatorId, gameMap);
            Map boardEvaluatorSettings = systemSettings.findBoardEvaluatorSettings(evaluatorId);
            Map evaluatorSettings = boardEvaluatorSettings(boardEvaluatorSettings, gameMap);
            Color color = colors.get(idx++ % colors.size());

            RaceGameSettings game = new RaceGameSettings(systemSettings, startBoard, parameterName,
                    parameters, gameMap, tetrisRulesId, pieceGeneratorId, evaluatorId,
                    evaluatorSettings, duration, color, level, numberOfKnownPieces, masterDepth, showAll);
            if (game.color != color) {
                // Don't consume the global color if a color was explicitly specified.
                idx--;
            }
            this.games.add(game);
        }
        initAreaPercentage();
        createGames();
    }

    private void createGames() {
        Iterator<RaceGameSettings> iterator = this.games.allGamesIterator();
        while (iterator.hasNext()) {
            RaceGameSettings settings = iterator.next();
            settings.createGame(settings.tetrisRules);
        }
    }

    public void resetSpeedometer() {
        for (RaceGameSettings settings : games) {
            settings.resetSpeedometer();
        }
    }

    public void decreaseAreaPercentage() {
        addAreaPercentage(-1);
    }

    public void increaseAreaPercentage() {
        addAreaPercentage(1);
    }

    public void addAreaPercentage(int direction) {
        if (startBoard.numberOfCells() < 200) {
            areaPercentage += direction;
        } else {
            areaPercentage += direction * -0.5;
        }
        if (areaPercentage < 0) {
            areaPercentage = 0;
        }
        if (areaPercentage > 100) {
            areaPercentage = 100;
        }
        initAreaPercentage();
    }

    public void initAreaPercentage() {
        for (RaceGameSettings game : games) {
            if (game.distribution != null) {
                game.distribution.setAreaPercentage(areaPercentage);
            }
        }
    }

    private String evaluatorId(String boardEvaluatorId, Map gameMap) {
        if (gameMap.containsKey("parameters")) {
            Object map = gameMap.get("parameters");
            if (map instanceof Map) {
                if (((Map)map).containsKey("board evaluator id")) {
                    return ((Map)map).get("board evaluator id").toString();
                }
            }
        }
        return boardEvaluatorId;
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
     * Add the 'board' (if exists) and all 'parameters' attributes to board evaluator settings.
     */
    private Map boardEvaluatorSettings(Map boardEvaluatorSettings, Map gameMap) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (gameMap.containsKey("board")) {
            result.put("board", gameMap.get("board"));
        }
        if (gameMap.containsKey("parameters")) {
            Object map = gameMap.get("parameters");
            if (map instanceof Map) {
                result.putAll((Map)map);
            }
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

        for (RaceGameSettings game : this.games.games) {
            GameState state = game.gameState;
            String heading = game.heading == null ? "" : "heading: " + game.heading + "\n";
            String parameterValue = game.parameterValue == null ? "" : "parameter value: " + game.parameterValue + "\n";
            String gameParameters = game.parameters == null ? "" : parameters(game.parameters, "     ");
            String level = "   level: " + state.level + "\n";
            String numberOfKnownPieces = "   number of known pieces: " + state.numberOfKnownPieces + "\n";
            String gamesLeft = state.gamesLeft == null ? "" : "   games left: " + state.gamesLeft + "\n";
            String piecesLeft = state.piecesLeft == null ? "" : "   pieces left: " + state.piecesLeft + "\n";
            String nextPieces = "   next pieces: " + game.nextPieces() + "\n";
            String duration = "   duration: " + game.duration + "\n";
            String usedParameters = boardEvaluatorParameters(state.boardEvaluator.parameters());
            String tetrisRuleId = game.tetrisRulesIdText == null ? "" : "   tetris rules id: " + game.tetrisRulesId + "\n";
            String pieceGeneratorId = game.pieceGeneratorIdText == null ? "" : "   piece generator id: " + game.pieceGeneratorIdText + "\n";
            String boardEvaluatorId = game.boardEvaluatorIdText == null ? "" : "   board evaluator id: " + game.boardEvaluatorIdText + "\n";
            String hide = "   hide: " + game.hide() + "\n";
            String paused = "   paused: " + game.paused() + "\n";
            String color = game.colorString == null ? "" : "   color: " + game.colorString + "\n";
            String startBoard = game.startBoardText == null ? "" : "   start board: " + game.startBoard.export(17) + "\n";
            String board = game.game.coloredBoard.export(11);
            String pieceGeneratorState = state.pieceGenerator.export();
            String distribution = state.distribution.export();
            String headValues = headValues(heading, parameterValue, gameParameters);

            String masterDepth = "   master depth: " + state.masterDepth + "\n";
            String totalEquityDiff = "   total equity diff: " + state.totalEquityDiff + "\n";
            String master = state.masterDepth == 0 ? "" : masterDepth + totalEquityDiff;

            games += headValues +
                    level +
                    numberOfKnownPieces +
                    gamesLeft +
                    piecesLeft +
                    master +
                    nextPieces +
                    duration +
                    hide +
                    paused +
                    tetrisRuleId +
                    pieceGeneratorId +
                    boardEvaluatorId +
                    color +
                    startBoard +
                    "   board: " + board + "\n" +
                    "   games: " + state.games() + "\n" +
                    "   pieces: " + state.pieces + "\n" +
                    "   pieces total: " + state.totalPieces + "\n" +
                    "   rows: " + state.rows + "\n" +
                    "   rows total: " + state.totalRows + "\n" +
                    "   min rows: " + state.minRows() + "\n" +
                    "   max rows: " + state.maxRows() + "\n" +
                    "   rows/game: " + state.rowsPerGameString() + "\n" +
                    "   piece/s: " + state.piecesPerSecond() + "\n" +
                    "   piece generator state: " + pieceGeneratorState + "\n" +
                    "   distribution: " + distribution + "\n" +
                    "   used parameters:\n" + usedParameters;
        }

        boolean isSimpleBoard = startBoardText.length() - startBoardText.replace(",", "").length() == 1;
        String board = isSimpleBoard ? "[" + startBoard.width + "," + startBoard.height + "]" : startBoard.export(14);
        String percentage = "area %: " + areaPercentage + "\n";

        String masterDepth = this.masterDepth == 0 ? "" : "master depth: " + this.masterDepth + "\n";
        String parameters = this.parameters == null ? "" : parameters(this.parameters, "  ");


        return "start board: " + board + "\n" +
               "tetris rules id: " + tetrisRulesId + "\n" +
               "piece generator id: " + pieceGeneratorId + "\n" +
               "board evaluator id: " + boardEvaluatorId + "\n" +
               "level: " + level + "\n" +
               "number of known pieces: " + numberOfKnownPieces + "\n" +
               masterDepth +
               "parameter name: " + parameterName + "\n" +
               parameters +
               "save on close: " + saveOnClose + "\n" +
               "restart on file change: " + restartOnFileChange + "\n" +
               colors +
               windowLocation.export() + "\n" +
               windows +
               percentage +
               games;
    }

    private String boardEvaluatorParameters(Map<String,String> parameters) {
        String result = "";

        for (Entry<String,String> entry : parameters.entrySet()) {
            result += "      " + entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return result;
    }

    private String headValues(String... values) {
        String result = "";

        int i;
        String separator = " - ";
        for (i=0; i<values.length && values[i].length() == 0; i++);
        for (int j=i; j<values.length; j++) {
            if (values[j].length() > 0) {
                result += separator + values[j];
                separator = "   ";
            }
        }
        return result;
    }

    private String parameters(Map parameterValues, String tab) {
        String result = "parameters:\n";

        Map orderedValues = new TreeMap(parameterValues);

        for (Object o : orderedValues.entrySet()) {
            Entry entry = (Entry)o;
            result += tab + entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return result;
    }

    @Override
    public String toString() {
        return reader.toString();
    }
}
