package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;

import static com.github.tetrisanalyzer.piece.Piece.*;
import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public abstract class GameSettings implements PieceSettings {
    public final int boardWidth;
    public final int boardHeight;
    public final int pieceStartX;
    public final int pieceStartY;
    public final boolean slidingEnabled;

    public abstract String id();
    public abstract String url();
    public abstract String description();

    public GameSettings(int boardWidth, int boardHeight, int pieceStartX, int pieceStartY, boolean slidingEnabled) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.pieceStartX = pieceStartX;
        this.pieceStartY = pieceStartY;
        this.slidingEnabled = slidingEnabled;
    }

    @Override
    public int pieceStartX() {
        return pieceStartX;
    }

    @Override
    public int pieceStartY() {
        return pieceStartY;
    }

    @Override
    public boolean isSlidingEnabled() {
        return slidingEnabled;
    }

    public AdjustmentDxDy dxdy(int dx, int dy) {
        return new AdjustmentDxDy(dx, dy);
    }

    public String export() {
        return new Settings("game rules",
                setting("id", id()),
                setting("url", url()),
                setting("description", description()),
                setting("sliding", (slidingEnabled ? "on" : "off")),
                setting("rotation", rotationDirection()),
                setting("piece start position on standard board", "[" + pieceStartX + "," + pieceStartY + "]"),
                setting("O", adjustment(O)),
                setting("I", adjustment(I)),
                setting("S", adjustment(S)),
                setting("Z", adjustment(Z)),
                setting("L", adjustment(L)),
                setting("J", adjustment(J)),
                setting("T", adjustment(T))).export();
    }

    private String adjustment(int piece) {
        return Piece.validPieces(this)[piece].adjustments.export();
    }

    @Override
    public String toString() {
        return export();
    }
}
