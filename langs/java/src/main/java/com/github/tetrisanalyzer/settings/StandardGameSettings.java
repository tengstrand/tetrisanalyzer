package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.Map;

import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

/**
 * Standard Tetris settings, introduced by Colin Fahey:
 *   http://colinfahey.com/tetris/tetris.html
 */
public class StandardGameSettings extends GameSettings {

    public StandardGameSettings(Board board) {
        this(board.width);
    }

    public StandardGameSettings(ColoredBoard board) {
        this(board.width);
    }

    public StandardGameSettings(Board board, boolean sliding) {
        this(board.width, sliding);
    }

    public StandardGameSettings(ColoredBoard board, boolean sliding) {
        this(board.width, sliding);
    }

    public StandardGameSettings(int boardWidth) {
        this(boardWidth, false);
    }

    public StandardGameSettings(Map settings) {
        this(boardWidth(settings), sliding(settings));
    }

    public StandardGameSettings(int boardWidth, boolean sliding) {
        super("Standard", "http://colinfahey.com/tetris/tetris.html",
                "Standard Tetris 2007 June 4 (Colin Fahey)", 3, 0, sliding, false, StandardGameSettings.class, adjustments());
    }

    private static Adjustments[] adjustments() {
        return new Adjustments[] {
                calculate("-", dxdy(0,0)),
                calculate("O", dxdy(1,1)),
                calculate("I", dxdy(0,1), dxdy(2,0)),
                calculate("S", dxdy(1,1), dxdy(2,0)),
                calculate("Z", dxdy(1,1), dxdy(2,0)),
                calculate("L", dxdy(1,1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                calculate("J", dxdy(1,1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                calculate("T", dxdy(1, 1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                calculate("x", dxdy(0,0)),
                calculate("+", dxdy(0,0))
        };
    }
}
