package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import static com.github.tetrisanalyzer.settings.StartPieceCalculator.startX;
import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

/**
 * Standard Tetris settings, introduced by Colin Fahey:
 *   http://colinfahey.com/tetris/tetris.html
 */
public class StandardGameSettings extends GameSettings {

    public StandardGameSettings(Board board) {
        this(board.width, board.height);
    }

    public StandardGameSettings(ColoredBoard board) {
        this(board.width, board.height);
    }

    public StandardGameSettings(Board board, boolean sliding) {
        this(board.width, board.height, sliding);
    }

    public StandardGameSettings(ColoredBoard board, boolean sliding) {
        this(board.width, board.height, sliding);
    }

    public StandardGameSettings(int boardWidth, int boardHeight) {
        this(boardWidth, boardHeight, false);
    }

    public StandardGameSettings(int boardWidth, int boardHeight, boolean sliding) {
        super(boardWidth, boardHeight, startX(boardWidth, 3), 0, sliding);
    }

    @Override public String id() { return "standard"; }
    @Override public String url() { return "http://colinfahey.com/tetris/tetris.html"; }
    @Override public String description() { return "Standard Tetris 2007 June 4 (Colin Fahey)"; }
    @Override public RotationDirection rotationDirection() { return new AnticlockwiseRotation(); }

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
                calculate("x", dxdy(0,0)),
                calculate("+", dxdy(0,0))
        };
    }
}
