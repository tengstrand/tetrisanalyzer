package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

/**
 * The default settings used by Tetris Analyzer.
 */
public class TetrisAnalyzerGameSettings extends AbstractGameSettings {

    public TetrisAnalyzerGameSettings() {
    }

    public TetrisAnalyzerGameSettings(boolean sliding) {
        super(sliding);
    }

    public TetrisAnalyzerGameSettings(int boardWidth, int boardHeight) {
        this(boardWidth, boardHeight, StartPieceCalculator.startX(boardWidth, 4), false);
    }

    public TetrisAnalyzerGameSettings(int boardWidth, int boardHeight, int pieceStartX) {
        super(boardWidth, boardHeight, pieceStartX);
    }

    public TetrisAnalyzerGameSettings(int boardWidth, int boardHeight, int pieceStartX, boolean sliding) {
        super(boardWidth, boardHeight, pieceStartX, sliding);
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
