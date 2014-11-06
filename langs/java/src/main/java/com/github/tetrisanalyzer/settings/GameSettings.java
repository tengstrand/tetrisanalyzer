package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import static com.github.tetrisanalyzer.piece.Piece.*;
import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public abstract class GameSettings implements PieceSettings {
    public final int pieceStartX;
    public final int pieceStartY;
    public final boolean slidingEnabled;
    public final Class clazz;
    public final Adjustments[] pieceAdjustments;

    public abstract String id();
    public abstract String url();
    public abstract String description();

    public GameSettings(int pieceStartX, int pieceStartY, boolean slidingEnabled, Class clazz, Adjustments[] pieceAdjustments) {
        this.pieceStartX = pieceStartX;
        this.pieceStartY = pieceStartY;
        this.slidingEnabled = slidingEnabled;
        this.clazz = clazz;
        this.pieceAdjustments = pieceAdjustments;
    }

    @Override
    public Adjustments[] pieceAdjustments() {
        return pieceAdjustments;
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

    public static AdjustmentDxDy dxdy(int dx, int dy) {
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
