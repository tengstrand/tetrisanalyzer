package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.game.GameState;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

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

    public RaceGameSettings(String parameterName, Map settings, PieceGenerator pieceGenerator, ColoredBoard mainBoard) {
        reader = new SettingsReader(settings, "game");

        Object parameterValue = reader.get("parameter value");

        ColoredBoard board = reader.readBoard();
        int movesLeft = 0; // TODO: Set value

        if (board == null && mainBoard == null) {
            throw new IllegalArgumentException("Expected to find attribute 'board' in 'race'");
        }
        game = new GameState(parameterName, parameterValue, board != null ? board : mainBoard, pieceGenerator, movesLeft);
    }
}
