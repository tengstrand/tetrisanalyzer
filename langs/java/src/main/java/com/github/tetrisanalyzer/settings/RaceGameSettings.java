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

/*
                "   duration: 1d 3h 52m 10.760s\n" +
                "   board: [10,12]\n" +
                "   games: 19308\n" +
                "   pieces: 153065282\n" +
                "   rows: 2666\n" +
                "   min rows: 2\n" +
                "   max rows: 27478\n" +
                "   rows/game: 23100\n" +
                "   cell area: 1.2342342\n" +
                "   pieces/s: 10987\n" +
                "   piece generator settings: {seed: 2345342142}\n" +
                "   distribution: [1,2,4,8,20,30,10,5,2,1,1]\n" +

     */

    public RaceGameSettings(String parameterName, Map settings, Map boardEvaluatorSettings,
                            PieceGenerator pieceGenerator, Duration mainDuration, ColoredBoard mainBoard) {
        reader = new SettingsReader(settings, "game");

        Object parameterValue = reader.get("parameter value");

        Duration duration = reader.readDuration();

        if (duration == null && mainDuration == null) {
            duration = Duration.create();
        }

        ColoredBoard board = reader.readBoard();
        int movesLeft = 0; // TODO: Set value

        if (board == null && mainBoard == null) {
            board = ColoredBoard.create(10, 20);
        }

        Map evaluatorSettings = evaluatorSettings(boardEvaluatorSettings, parameterName, parameterValue);
        BoardEvaluator boardEvaluator = createBoardEvaluator(evaluatorSettings);

        game = new GameState(duration != null ? duration : mainDuration,
                board != null ? board : mainBoard, boardEvaluator, pieceGenerator, movesLeft);
    }

    private Map evaluatorSettings(Map boardEvaluatorSettings, String parameterName, Object parameterValue) {
        Map result = new HashMap();
        result.putAll(boardEvaluatorSettings);
        if (parameterName != null) {
            result.put(parameterName, parameterValue);
        }
        return result;
    }

    private BoardEvaluator createBoardEvaluator(Map settings) {
        SettingsReader mapReader = new SettingsReader(settings, "piece generator");
        Class clazz = mapReader.readClass("class");

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


}
