package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;

import static com.github.tetrisanalyzer.piece.Piece.*;

public abstract class AbstractGameSettings implements GameSettings {
    private final int boardWidth;
    private final int boardHeight;
    private final int pieceStartX;
    private final boolean sliding;

    public AbstractGameSettings(int boardWidth, int boardHeight, int pieceStartX, boolean sliding) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.pieceStartX = pieceStartX;
        this.sliding = sliding;
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
    public String export() {
        return "game rules:" +
                "\n  id: " + id() +
                "\n  url: " + url() +
                "\n  description: " + description() +
                "\n  sliding: " + sliding +
                "\n  rotation: " + rotationDirection() +
                "\n  piece start x on standard board: " + pieceStartX +
                "\n  " + adjustment(O) +
                "\n  " + adjustment(I) +
                "\n  " + adjustment(S) +
                "\n  " + adjustment(Z) +
                "\n  " + adjustment(L) +
                "\n  " + adjustment(J) +
                "\n  " + adjustment(T) + "\n";
    }

    private String adjustment(int piece) {
        return Piece.validPieces(this)[piece].adjustments.export();
    }

    @Override
    public String toString() {
        return export();
    }
}
