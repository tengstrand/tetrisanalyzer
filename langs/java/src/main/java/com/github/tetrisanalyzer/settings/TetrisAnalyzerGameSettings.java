package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.piece.Piece;

import static com.github.tetrisanalyzer.piece.Piece.*;

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
    public int[] startPieceDx() {
        return new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }

    @Override
    public int[][] pieceDx() {
        int[][] result = new int[Piece.NUMBER_OF_PIECE_TYPES + 2][];
        result[I] = new int[] { 0, 0 };
        result[Z] = new int[] { 0, 0 };
        result[S] = new int[] { 0, 0 };
        result[J] = new int[] { 0, 0, 0, 0 };
        result[L] = new int[] { 0, 0, 0, 0 };
        result[T] = new int[] { 0, 0, 0, 0 };
        result[O] = new int[] { 0 };
        return result;
    }

    @Override
    public int[][] pieceDy() {
        int[][] result = new int[Piece.NUMBER_OF_PIECE_TYPES + 2][];
        result[I] = new int[] { 0, 0 };
        result[Z] = new int[] { 0, 0 };
        result[S] = new int[] { 0, 0 };
        result[J] = new int[] { 0, 0, 0, 0 };
        result[L] = new int[] { 0, 0, 0, 0 };
        result[T] = new int[] { 0, 0, 0, 0 };
        result[O] = new int[] { 0 };
        return result;
    }
}
