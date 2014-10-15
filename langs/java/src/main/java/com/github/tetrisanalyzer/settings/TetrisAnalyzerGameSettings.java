package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import static com.github.tetrisanalyzer.settings.StartPieceCalculator.startX;
import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

/**
 * The default settings used by Tetris Analyzer.
 */
public class TetrisAnalyzerGameSettings extends AbstractGameSettings {

    public TetrisAnalyzerGameSettings(Board board) {
        this(board.width, board.height);
    }

    public TetrisAnalyzerGameSettings() {
        this(10, 20);
    }

    public TetrisAnalyzerGameSettings(boolean sliding) {
        this(10, 20, sliding);
    }

    public TetrisAnalyzerGameSettings(int boardWidth, int boardHeight) {
        this(boardWidth, boardHeight, false);
    }

    public TetrisAnalyzerGameSettings(int boardWidth, int boardHeight, boolean sliding) {
        super(boardWidth, boardHeight, startX(boardWidth, 4), sliding);
    }

    @Override
    public String name() {
        return "Tetris Analyzer (Joakim Tengstrand)";
    }

    @Override
    public Adjustments[] pieceAdjustments() {
        return new Adjustments[] {
                calculate("-", dxdy(0,0)),
                calculate("O", dxdy(0,0)),
                calculate("I", dxdy(0,0), dxdy(0,0)),
                calculate("S", dxdy(0,0), dxdy(0,0)),
                calculate("Z", dxdy(0,0), dxdy(0,0)),
                calculate("L", dxdy(0,0), dxdy(0,0), dxdy(0,0), dxdy(0,0)),
                calculate("J", dxdy(0,0), dxdy(0,0), dxdy(0,0), dxdy(0,0)),
                calculate("T", dxdy(0,0), dxdy(0,0), dxdy(0,0), dxdy(0,0)),
                calculate("x", dxdy(0,0))
        };
    }
}
