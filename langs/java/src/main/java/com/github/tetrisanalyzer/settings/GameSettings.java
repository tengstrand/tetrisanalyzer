package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.List;
import java.util.Map;

import static com.github.tetrisanalyzer.piece.Piece.*;
import static com.github.tetrisanalyzer.settings.Setting.setting;

/**
 * This interface defines the API of the game settings used by the Tetris engine.
 */
public class GameSettings extends PieceSettings {
    public final String id;
    public final String url;
    public final String description;
    public final int pieceStartX;
    public final int pieceStartY;
    public final boolean slidingEnabled;
    public final Class clazz;

    public GameSettings(String id, String url, String description, int pieceStartX, int pieceStartY, boolean slidingEnabled, boolean clockwise, Class clazz, Adjustments[] pieceAdjustments) {
        super(clockwise, slidingEnabled, pieceStartX, pieceStartY, pieceAdjustments);
        this.id = id;
        this.url = url;
        this.description = description;
        this.pieceStartX = pieceStartX;
        this.pieceStartY = pieceStartY;
        this.slidingEnabled = slidingEnabled;
        this.clazz = clazz;
    }

    public static AdjustmentDxDy dxdy(int dx, int dy) {
        return new AdjustmentDxDy(dx, dy);
    }

    public String export() {
        return new Settings("game rules",
                setting("id", id),
                setting("url", url),
                setting("description", description),
                setting("sliding", (slidingEnabled ? "on" : "off")),
                setting("rotation", rotationDirection),
                setting("piece start position on standard board", "[" + pieceStartX + "," + pieceStartY + "]"),
                setting("O", adjustment(O)),
                setting("I", adjustment(I)),
                setting("S", adjustment(S)),
                setting("Z", adjustment(Z)),
                setting("L", adjustment(L)),
                setting("J", adjustment(J)),
                setting("T", adjustment(T))).export();
    }

    public static int boardWidth(Map settings) {
        return boardSize(settings, "game rule").get(0);
    }

    public static boolean sliding(Map settings) {
        return reader(settings, "game rule").readString("sliding", "on", "off").equals("on");
    }

    private static SettingsReader reader(Map settings, String group) {
        return new SettingsReader(settings, group);
    }

    private static List<Integer> boardSize(Map settings, String group) {
        return reader(settings, group).readIntegers("board");
    }

    private String adjustment(int piece) {
        return Piece.validPieces(this)[piece].adjustments.export();
    }

    @Override
    public String toString() {
        return export();
    }
}
