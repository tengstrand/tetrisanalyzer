package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;

import static com.github.tetrisanalyzer.piece.Piece.*;

/**
 * The default settings used by the Tetris engine.
 */
public class DefaultGameSettings implements GameSettings {

    private final int boardWidth;
    private final int boardHeight;
    private final int pieceStartX;

    public DefaultGameSettings() {
        this(10, 20, 4);
    }

    public DefaultGameSettings(int boardWidth, int boardHeight, int pieceStartX) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.pieceStartX = pieceStartX;
    }

    public boolean isSlidingEnabled() {
        return false;
    }

    public RotationDirection rotationDirection() {
        return new AnticlockwiseRotation();
    }

    @Override
    public int pieceStartX() {
        return pieceStartX;
    }

    @Override
    public int boardWidth() {
        return boardWidth;
    }

    @Override
    public int boardHeight() {
        return boardHeight;
    }

    @Override
    public int[] startPieceDx() {
        return new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    }

    @Override
    public int[] startPieceDy() {
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
