package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;

import static com.github.tetrisanalyzer.piece.Piece.*;
import static com.github.tetrisanalyzer.piece.Piece.O;
import static com.github.tetrisanalyzer.piece.Piece.T;

public abstract class AbstractGameSettings implements GameSettings {

    private final int boardWidth;
    private final int boardHeight;
    private final int pieceStartX;
    private final boolean sliding;

    public AbstractGameSettings() {
        this(10, 20, 4);
    }

    public AbstractGameSettings(boolean sliding) {
        this(10, 20, 4, sliding);
    }

    public AbstractGameSettings(int boardWidth, int boardHeight, int pieceStartX) {
        this(boardWidth, boardHeight, pieceStartX, false);
    }

    public AbstractGameSettings(int boardWidth, int boardHeight, int pieceStartX, boolean sliding) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.pieceStartX = pieceStartX;
        this.sliding = sliding;
    }

    @Override
    public String info() {
        return name() + "\n" +
                "Board: " + boardWidth + " x " + boardHeight + "\n" +
                "Sliding: " + isSlidingEnabled() +  "\n" +
                "Rotation: " + rotationDirection() + "\n" +
                "Piece start position x: " + pieceStartX() + "\n" +
                "Delta I: " + (delta(pieceDx()[I], pieceDy()[I])) + "\n" +
                "Delta Z: " + (delta(pieceDx()[Z], pieceDy()[Z])) + "\n" +
                "Delta S: " + (delta(pieceDx()[S], pieceDy()[S])) + "\n" +
                "Delta J: " + (delta(pieceDx()[J], pieceDy()[J])) + "\n" +
                "Delta L: " + (delta(pieceDx()[L], pieceDy()[L])) + "\n" +
                "Delta T: " + (delta(pieceDx()[T], pieceDy()[T])) + "\n" +
                "Delta O: " + (delta(pieceDx()[O], pieceDy()[O])) + "\n" ;
    }

    private String delta(int[] dx, int[] dy) {
        String result = "";
        String separator = "";

        for (int i=0; i<dx.length; i++) {
            result += separator + "(" + dx[i] + "," + dy[i] + ")";
            separator = " ";
        }
        return result;
    }

    public boolean isSlidingEnabled() {
        return sliding;
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

    public RotationDirection rotationDirection() {
        return new AnticlockwiseRotation();
    }
}
