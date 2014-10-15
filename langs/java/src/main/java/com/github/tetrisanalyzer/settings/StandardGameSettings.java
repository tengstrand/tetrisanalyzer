package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import static com.github.tetrisanalyzer.settings.StartPieceCalculator.startX;
import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

/**
 * Settings introduced by Colin Fahey:
 *   http://colinfahey.com/tetris/tetris.html
 */
public class StandardGameSettings extends AbstractGameSettings {

    public StandardGameSettings(Board board) {
        this(board.width, board.height);
    }

    public StandardGameSettings(int boardWidth, int boardHeight) {
        this(boardWidth, boardHeight, false);
    }

    public StandardGameSettings(int boardWidth, int boardHeight, boolean sliding) {
        super(boardWidth, boardHeight, startX(boardWidth, 3), sliding);
    }

    @Override
    public String name() {
        return "Standard Tetris (Colin Fahey)";
    }

    @Override
    public Adjustments[] pieceAdjustments() {
        return new Adjustments[] {
                calculate("-", dxdy(0,0)),
                calculate("O", dxdy(1,1)),
                calculate("I", dxdy(0,1), dxdy(2,0)),
                calculate("S", dxdy(1,1), dxdy(2,0)),
                calculate("Z", dxdy(1,1), dxdy(2,0)),
                calculate("L", dxdy(1,1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                calculate("J", dxdy(1,1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                calculate("T", dxdy(1, 1), dxdy(2,0), dxdy(1,0), dxdy(1,0)),
                calculate("x", dxdy(0,0))
        };
    }
}
