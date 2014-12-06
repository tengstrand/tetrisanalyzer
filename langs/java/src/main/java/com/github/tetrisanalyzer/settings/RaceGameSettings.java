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
    private final SettingsReader reader;
    public Game game;
    public final GameState gameState;

    public Object parameterValue;
    public Color color;
    public Duration duration;
    public Distribution distribution;

    public String tetrisRulesId;
    public String pieceGeneratorId;
    public String boardEvaluatorId;

    public GameSettings tetrisRules;
    public PieceGenerator pieceGenerator;
    public BoardEvaluator boardEvaluator;

    public RaceGameSettings(SystemSettings systemSettings, ColoredBoard raceBoard, String parameterName, Map settings,
                            String tetrisRulesId, String pieceGeneratorId, String boardEvaluatorId,
                            Map boardEvaluatorSettings, Duration mainDuration, Color color) {
        reader = new SettingsReader(settings, "game");

        this.color = color;
        this.pieceGeneratorId = pieceGeneratorId;
        this.boardEvaluatorId = boardEvaluatorId;

        parameterValue = reader.get("parameter value");

        if (reader.exists("tetris rules id")) {
            this.tetrisRulesId = reader.readString("tetris rules id");
        } else {
            this.tetrisRulesId = tetrisRulesId;
        }
        tetrisRules = systemSettings.findTetrisRules(tetrisRulesId);

        if (reader.exists("piece generator id")) {
            this.pieceGeneratorId = reader.readString("piece generator id");
        } else {
            this.pieceGeneratorId = pieceGeneratorId;
        }
        Map pieceGeneratorSettings = systemSettings.findPieceGeneratorSettings(this.pieceGeneratorId);

        duration = reader.readDuration();

        if (duration == null) {
            if (mainDuration != null) {
                duration = mainDuration;
            } else {
                duration = Duration.create();
            }
        }

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

        Map evaluatorSettings = evaluatorSettings(boardEvaluatorSettings, parameterName, parameterValue);
        boardEvaluator = createBoardEvaluator(board.width, board.height, evaluatorSettings);

        gameState = new GameState(duration, board, distribution,
                boardEvaluator, pieceGenerator, games, pieces,
                totalPieces, rows, totalRows, minRows, maxRows, piecesLeft);
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

    private Map evaluatorSettings(Map boardEvaluatorSettings, String parameterName, Object parameterValue) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (parameterName != null && parameterValue != null) {
            result.put(parameterName, parameterValue);
        }
        return result;
    }

    private BoardEvaluator createBoardEvaluator(int boardWidth, int boardHeight, Map settings) {
        Class clazz = classAttribute(settings);

        try {
            Constructor constructor = clazz.getConstructor(int.class, int.class, Map.class);
            return (BoardEvaluator)constructor.newInstance(boardWidth, boardHeight, settings);
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
        game = new Game(gameState, tetrisRules);
        return game;
    }
}
