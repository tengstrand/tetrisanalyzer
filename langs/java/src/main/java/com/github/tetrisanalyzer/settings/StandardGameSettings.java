package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.piece.Piece;

import static com.github.tetrisanalyzer.piece.Piece.*;
import static com.github.tetrisanalyzer.piece.Piece.O;

/**
 * Settings introduced by Colin Fahey:
 *   http://colinfahey.com/tetris/tetris.html
 */
public class StandardGameSettings extends AbstractGameSettings {

    public StandardGameSettings() {
        super();
    }

    public StandardGameSettings(boolean sliding) {
        super(sliding);
    }

    public StandardGameSettings(int boardWidth, int boardHeight) {
        this(boardWidth, boardHeight, StartPieceCalculator.startX(boardWidth, 3), false);
    }

    public StandardGameSettings(int boardWidth, int boardHeight, int pieceStartX) {
        super(boardWidth, boardHeight, pieceStartX);
    }

    public StandardGameSettings(int boardWidth, int boardHeight, int pieceStartX, boolean sliding) {
        super(boardWidth, boardHeight, pieceStartX, sliding);
    }

    @Override
    public String name() {
        return "Standard Tetris (Colin Fahey)";
    }

    @Override
    public int[] pieceStartDx() {
        return new int[] { 0, 0, 1, 1, 1, 1, 1, 1, 0 };
    }

    @Override
    public int[][] pieceDx() {
        int[][] result = new int[Piece.NUMBER_OF_PIECE_TYPES + 2][];
        result[I] = new int[] { 2, -2 };
        result[Z] = new int[] { 1, -1 };
        result[S] = new int[] { 1, -1 };
        result[J] = new int[] { 1, -1, 0, 0 };
        result[L] = new int[] { 1, -1, 0, 0 };
        result[T] = new int[] { 1, -1, 0, 0 };
        result[O] = new int[] { 0 };
        return result;
    }

    @Override
    public int[][] pieceDy() {
        int[][] result = new int[Piece.NUMBER_OF_PIECE_TYPES + 2][];
        result[I] = new int[] { -1, 1 };
        result[Z] = new int[] { -1, 1 };
        result[S] = new int[] { -1, 1 };
        result[J] = new int[] { -1, 0, 0, 1 };
        result[L] = new int[] { -1, 0, 0, 1 };
        result[T] = new int[] { -1, 0, 0, 1 };
        result[O] = new int[] { 0 };
        return result;
    }
}
