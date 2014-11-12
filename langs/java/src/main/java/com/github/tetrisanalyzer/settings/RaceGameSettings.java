package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.game.Game;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RaceGameSettings {
    private final SettingsReader reader;
    public Game game;
    public final GameState gameState;

    public Object parameterValue;
    public Duration duration;

    public PieceGenerator pieceGenerator;
    public BoardEvaluator boardEvaluator;

    public RaceGameSettings(String parameterName, Map settings, Map boardEvaluatorSettings,
                            Map pieceGeneratorSettings, Duration mainDuration, ColoredBoard mainBoard) {
        reader = new SettingsReader(settings, "game");

        parameterValue = reader.get("parameter value");
        duration = reader.readDuration();

        if (duration == null) {
            if (mainDuration != null) {
                duration = mainDuration;
            } else {
                duration = Duration.create();
            }
        }

        long numberOfGames = reader.readLong("games");
        long numberOfPieces = reader.readLong("pieces");
        long totalNumberOfPieces = reader.readLong("pieces total");
        long totalNumberOfRows = reader.readLong("rows");

        long minRows = reader.readLong("min rows", Long.MAX_VALUE);
        long maxRows = reader.readLong("max rows", Long.MIN_VALUE);

        Map generatorSettings = pieceGeneratorSettings(pieceGeneratorSettings, reader.readMap("piece generator state"));
        pieceGenerator = createPieceGenerator(generatorSettings);

        ColoredBoard board = reader.readBoard();
        int piecesLeft = 0; // TODO: Set value

        if (board == null && mainBoard == null) {
            board = ColoredBoard.create(10, 20);
        }

        Map evaluatorSettings = evaluatorSettings(boardEvaluatorSettings, parameterName, parameterValue);
        boardEvaluator = createBoardEvaluator(evaluatorSettings);

        gameState = new GameState(duration, board != null ? board : mainBoard,
                boardEvaluator, pieceGenerator, numberOfGames, numberOfPieces,
                totalNumberOfPieces, totalNumberOfRows, minRows, maxRows, piecesLeft);
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
            throw new IllegalArgumentException(e);
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

    private BoardEvaluator createBoardEvaluator(Map settings) {
        Class clazz = classAttribute(settings);

        try {
            Constructor constructor = clazz.getConstructor(Map.class);
            return (BoardEvaluator)constructor.newInstance(settings);
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
