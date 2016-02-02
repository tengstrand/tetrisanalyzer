package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.game.Distribution;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

public class RaceGameSettings {
    public Thread thread;

    private final SettingsReader reader;
    public int masterDepth;
    public double totalEquityDiff;
    private boolean hide;
    private boolean paused;
    public int level;
    public int numberOfKnownPieces;
    public List<String> nextPieces;

    public Game game;
    public final GameState gameState;

    public String startBoardText;
    public ColoredBoard startBoard;

    public String heading;
    public Object parameterValue;
    public Map parameters;
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

    public RaceGameSettings(SystemSettings systemSettings, ColoredBoard startBoard, String parameterName,
                            Map parentParameters, Map settings, String tetrisRulesId, String pieceGeneratorId,
                            String boardEvaluatorId, Map boardEvaluatorSettings, Duration mainDuration, Color color,
                            int level, int numberOfKnownPieces, int masterDepth, boolean showAll) {
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
        if (parameterName == null || parameterName.isEmpty()) {
            parameterValue = null;
        }

        this.parameters = reader.readMap("parameters", null);

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
        this.level = reader.readInteger("level", level);
        this.numberOfKnownPieces = reader.readInteger("number of known pieces", numberOfKnownPieces);
        if (this.numberOfKnownPieces > level) {
            this.numberOfKnownPieces = level;
        }
        this.masterDepth = reader.readInteger("master depth", masterDepth);
        nextPieces = reader.readList("next pieces", Arrays.asList());
        hide = showAll ? false : reader.readBoolean("hide", false);
        paused = reader.readBoolean("paused", false);
        totalEquityDiff = reader.readDouble("total equity diff", 0);

        Long gamesToPlay = reader.readLong("games to play", null);
        Long piecesToPlay = reader.readLong("pieces to play", null);
        long games = reader.readLong("games", 0L);
        long pieces = reader.readLong("pieces", 0L);
        long totalPieces = reader.readLong("pieces total", 0L);
        long rows = reader.readLong("rows", 0L);
        long totalRows = reader.readLong("rows total", 0L);

        long minRows = reader.readLong("min rows", Long.MAX_VALUE);
        long maxRows = reader.readLong("max rows", Long.MIN_VALUE);

        Map generatorSettings = pieceGeneratorSettings(pieceGeneratorSettings, reader.readMap("piece generator state", new HashMap<>()));
        pieceGenerator = createPieceGenerator(generatorSettings);

        startBoardText = reader.readString("start board", null);
        this.startBoard = reader.readBoard("start board", startBoard);

        if (this.startBoard == null) {
            throw new IllegalArgumentException("The 'start board' must be set");
        }
        ColoredBoard board = reader.readBoard("board", this.startBoard);

        distribution = reader.readDistribution(board.width, board.height);

        if (this.startBoard.width != board.width || this.startBoard.height != board.height) {
            throw new IllegalArgumentException("The size of 'start board' and 'board' must match");
        }
        Map evaluatorSettings = evaluatorSettings(boardEvaluatorSettings, parameterName, parentParameters);
        boardEvaluator = createBoardEvaluator(board.width, board.height, tetrisRules, evaluatorSettings);

        gameState = new GameState(duration, board, this.startBoard, distribution, boardEvaluator, pieceGenerator,
                this.masterDepth, totalEquityDiff, this.level, this.numberOfKnownPieces, nextPieces,
                games, pieces, totalPieces, rows, totalRows, minRows, maxRows, gamesToPlay, piecesToPlay);
    }

    public void resetSpeedometer() {
        gameState.resetSpeedometer();
    }

    public boolean paused() {
        if (game != null) {
            return game.paused;
        }
        return paused;
    }

    public boolean hide() {
        if (game != null) {
            return game.hide;
        }
        return hide;
    }

    public List<String> nextPieces() {
        if (game == null) {
            return nextPieces;
        }
        List<String> result = new ArrayList<>();

        for (Piece piece : game.nextPieces.pieces) {
            result.add(Character.toString(piece.character()));
        }
        return result;
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

    private Map evaluatorSettings(Map boardEvaluatorSettings, String parameterName, Map parentParameters) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (parentParameters != null) {
            result.putAll(parentParameters);
        }
        if (parameters != null) {
            result.putAll(parameters);
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
        game = new Game(gameState, tetrisRules, paused(), hide());
        thread = new Thread(game);
        return game;
    }
}
