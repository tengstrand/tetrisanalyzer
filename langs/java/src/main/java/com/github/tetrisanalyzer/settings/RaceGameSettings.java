package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.game.Distribution;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RaceGameSettings {
    public Thread thread;

    private final SettingsReader reader;
    public boolean permanentlyPaused;
    public Game game;
    public final GameState gameState;

    public String heading;
    public Object parameterValue;
    public Map parameterValues;
    public String colorString;
    public Color color;
    public Duration duration;
    public Distribution distribution;

    public String tetrisRulesId;
    public String pieceGeneratorId;
    public String boardEvaluatorId;

    public String tetrisRulesIdText;
    public String pieceGeneratorIdText;
    public String boardEvaluatorIdText;

    public GameSettings tetrisRules;
    public PieceGenerator pieceGenerator;
    public BoardEvaluator boardEvaluator;

    public RaceGameSettings(SystemSettings systemSettings, ColoredBoard raceBoard, String parameterName, Map settings,
                            String tetrisRulesId, String pieceGeneratorId, String boardEvaluatorId,
                            Map boardEvaluatorSettings, Duration mainDuration, Color color) {
        reader = new SettingsReader(settings, "game");

        this.color = reader.readColor("color", color);
        colorString = reader.exists("color") ? reader.readString("color") : null;

        tetrisRulesIdText = reader.readString("tetris rules id", null);
        this.tetrisRulesId = reader.readString("tetris rules id", tetrisRulesId);

        pieceGeneratorIdText = reader.readString("piece generator id", null);
        this.pieceGeneratorId = reader.readString("piece generator id", pieceGeneratorId);

        boardEvaluatorIdText = reader.readString("board evaluator id", null);
        this.boardEvaluatorId = reader.readString("board evaluator id", boardEvaluatorId);

        heading = reader.readString("heading", null);
        parameterValue = reader.get("parameter value");
        if (parameterName == null) {
            parameterValue = null;
        }

        parameterValues = reader.readMap("parameter values", null);

        tetrisRules = systemSettings.findTetrisRules(tetrisRulesId);

        Map pieceGeneratorSettings = systemSettings.findPieceGeneratorSettings(this.pieceGeneratorId);

        duration = reader.readDuration();

        if (duration == null) {
            if (mainDuration != null) {
                duration = mainDuration;
            } else {
                duration = Duration.create();
            }
        }
        permanentlyPaused = reader.readBoolean("paused", false);

        long games = reader.readLong("games", 0);
        long pieces = reader.readLong("pieces", 0);
        long totalPieces = reader.readLong("pieces total", 0);
        long rows = reader.readLong("rows", 0);
        long totalRows = reader.readLong("rows total", 0);

        long minRows = reader.readLong("min rows", Long.MAX_VALUE);
        long maxRows = reader.readLong("max rows", Long.MIN_VALUE);

        Map generatorSettings = pieceGeneratorSettings(pieceGeneratorSettings, reader.readMap("piece generator state", new HashMap<>()));
        pieceGenerator = createPieceGenerator(generatorSettings);

        ColoredBoard gameBoard = reader.readBoard();
        int piecesLeft = 0;

        if (gameBoard == null && raceBoard == null) {
            gameBoard = ColoredBoard.create(10, 20);
        }
        ColoredBoard board = gameBoard != null ? gameBoard : raceBoard;

        distribution = reader.readDistribution(board.width, board.height);

        Map evaluatorSettings = evaluatorSettings(boardEvaluatorSettings, parameterName, parameterValue, parameterValues);
        boardEvaluator = createBoardEvaluator(board.width, board.height, tetrisRules, evaluatorSettings);

        gameState = new GameState(duration, board, distribution,
                boardEvaluator, pieceGenerator, games, pieces,
                totalPieces, rows, totalRows, minRows, maxRows, piecesLeft);
    }

    public String heading() {
        if (heading != null) {
            return heading;
        }
        return parameterValue == null ? "" : parameterValue.toString();
    }

    private Map pieceGeneratorSettings(Map pieceGeneratorSettings, Map parameters) {
        Map result = new HashMap();
        result.putAll(pieceGeneratorSettings);
        if (parameters != null) {
            result.putAll(parameters);
        }
        return result;
    }

    private PieceGenerator createPieceGenerator(Map settings) {
        Class clazz = classAttribute(settings);

        try {
            Constructor constructor = clazz.getConstructor(Map.class);
            return (PieceGenerator)constructor.newInstance(settings);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getCause());
        }
    }

    private Map evaluatorSettings(Map boardEvaluatorSettings, String parameterName, Object parameterValue, Map parameterValues) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (parameterValues != null) {
            result.putAll(parameterValues);
        }
        if (parameterName != null && parameterValue != null) {
            result.put(parameterName, parameterValue);
        }
        return result;
    }

    private BoardEvaluator createBoardEvaluator(int boardWidth, int boardHeight, GameSettings rules, Map settings) {
        Class clazz = classAttribute(settings);

        try {
            Constructor constructor = clazz.getConstructor(int.class, int.class, GameSettings.class, Map.class);
            return (BoardEvaluator)constructor.newInstance(boardWidth, boardHeight, rules, settings);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e.getTargetException());
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Class classAttribute(Map settings) {
        SettingsReader mapReader = new SettingsReader(settings, "piece generator");
        return mapReader.readClass("class");
    }

    public Game createGame(GameSettings tetrisRules) {
        game = new Game(gameState, tetrisRules, permanentlyPaused);
        return game;
    }
}
