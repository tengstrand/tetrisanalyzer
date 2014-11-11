package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RaceGameSettings {
    private final SettingsReader reader;
    private final GameState game;

    public Duration duration;
    public long numberOfGames;
    public long numberOfPieces;
    public long totalNumberOfRows;
    public int minRows;
    public int maxRows;

    public PieceGenerator pieceGenerator;
    public BoardEvaluator boardEvaluator;

    public RaceGameSettings(String parameterName, Map settings, Map boardEvaluatorSettings,
                            Map pieceGeneratorSettings, Duration mainDuration, ColoredBoard mainBoard) {
        reader = new SettingsReader(settings, "game");

        Object parameterValue = reader.get("parameter value");

        duration = reader.readDuration();

        if (duration == null && mainDuration == null) {
            duration = Duration.create();
        }

        numberOfGames = reader.readLong("games");
        numberOfPieces = reader.readLong("pieces");
        totalNumberOfRows = reader.readLong("rows");

        minRows = reader.readInteger("min rows", Integer.MAX_VALUE);
        maxRows = reader.readInteger("max rows", Integer.MIN_VALUE);

        Map generatorSettings = pieceGeneratorSettings(pieceGeneratorSettings, reader.readMap("piece generator state"));
        pieceGenerator = createPieceGenerator(generatorSettings);

        ColoredBoard board = reader.readBoard();
        int movesLeft = 0; // TODO: Set value

        if (board == null && mainBoard == null) {
            board = ColoredBoard.create(10, 20);
        }

        Map evaluatorSettings = evaluatorSettings(boardEvaluatorSettings, parameterName, parameterValue);
        boardEvaluator = createBoardEvaluator(evaluatorSettings);

        game = new GameState(duration != null ? duration : mainDuration,
                board != null ? board : mainBoard, boardEvaluator, pieceGenerator, movesLeft);
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
}
