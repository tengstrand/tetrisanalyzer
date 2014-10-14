package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;

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
    public String export() {
        Piece[] pieces = Piece.pieces(this);

        return "";

/*        return "Game-settings {" +
                "\n  Name: " + name() +
                "\n  Board: " + boardWidth + " x " + boardHeight +
                "\n  Sliding: " + isSlidingEnabled() +
                "\n  Rotation: " + rotationDirection() +
                "\n  Piece start position x: " + pieceStartX() +
//                "\n  Piece start position dx: " + positionDx() +
                "\n  Delta I: " + (delta(pieceDx()[I], pieceDy()[I], pieces)) +
                "\n  Delta Z: " + (delta(pieceDx()[Z], pieceDy()[Z], pieces)) +
                "\n  Delta S: " + (delta(pieceDx()[S], pieceDy()[S], pieces)) +
                "\n  Delta J: " + (delta(pieceDx()[J], pieceDy()[J], pieces)) +
                "\n  Delta L: " + (delta(pieceDx()[L], pieceDy()[L], pieces)) +
                "\n  Delta T: " + (delta(pieceDx()[T], pieceDy()[T], pieces)) +
                "\n  Delta O: " + (delta(pieceDx()[O], pieceDy()[O], pieces));
    */
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

    public AdjustmentDxDy dxdy(int dx, int dy) {
        return new AdjustmentDxDy(dx, dy);
    }

    @Override
    public String toString() {
        return export();
    }
}
