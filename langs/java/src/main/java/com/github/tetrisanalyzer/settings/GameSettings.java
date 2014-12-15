package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.List;
import java.util.Map;

/**
 * Defines the API of the game settings used by the Tetris engine.
 */
public class GameSettings extends PieceSettings {
    public final String id;
    public final String url;
    public final String description;
    private final int pieceStartXOnStandardBoard;
    public final int pieceStartY;
    public final boolean slidingEnabled;
    public final Class clazz;

    public GameSettings(String id, String url, String description, int pieceStartXOnStandardBoard, int pieceStartY, boolean slidingEnabled, boolean clockwise, Class clazz, Adjustments[] pieceAdjustments) {
        super(clockwise, slidingEnabled, pieceStartXOnStandardBoard, pieceStartY, pieceAdjustments);
        this.id = id;
        this.url = url;
        this.description = description;
        this.pieceStartXOnStandardBoard = pieceStartXOnStandardBoard;
        this.pieceStartY = pieceStartY;
        this.slidingEnabled = slidingEnabled;
        this.clazz = clazz;
    }

    public static AdjustmentDxDy dxdy(int dx, int dy) {
        return new AdjustmentDxDy(dx, dy);
    }

    public static int boardWidth(Map settings) {
        return boardSize(settings, "game rule").get(0);
    }

    public static boolean sliding(Map settings) {
        return reader(settings, "game rule").readStringEnsureValue("sliding", "on", "off").equals("on");
    }

    private static SettingsReader reader(Map settings, String group) {
        return new SettingsReader(settings, group);
    }

    private static List<Integer> boardSize(Map settings, String group) {
        return reader(settings, group).readIntegers("board");
    }

    public int pieceStartX(int boardWidth) {
        return StartPieceCalculator.startX(boardWidth, pieceStartXOnStandardBoard);
    }
}
