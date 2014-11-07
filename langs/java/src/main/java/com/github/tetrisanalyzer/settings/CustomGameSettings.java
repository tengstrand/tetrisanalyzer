package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.tetrisanalyzer.settings.SettingsFunctions.*;
import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

public class CustomGameSettings extends GameSettings {

    public static CustomGameSettings fromMap(Map settings) {
        String id = id(settings);
        String url = url(settings);
        String description = description(settings);
        boolean sliding = sliding(settings);
        boolean clockwise = clockwise(settings);
        PieceStartPos pieceStartPos = pieceStartPos(settings);
        Class clazz = classname(settings);

        Adjustments empty = calculate("-", dxdy(0,0));
        Adjustments O = adjustments("O", settings);
        Adjustments I = adjustments("I", settings);
        Adjustments S = adjustments("S", settings);
        Adjustments Z = adjustments("Z", settings);
        Adjustments L = adjustments("L", settings);
        Adjustments J = adjustments("J", settings);
        Adjustments T = adjustments("T", settings);
        Adjustments any = calculate("-", dxdy(0,0));
        Adjustments shadow = calculate("-", dxdy(0,0));

        Adjustments[] pieceAdjustments = new Adjustments[] { empty, O, I, S, Z, L, J, T, any, shadow };

        return new CustomGameSettings(id, url, description, pieceStartPos.x, pieceStartPos.y, sliding, clockwise, clazz, pieceAdjustments);
    }

    private static String id(Map settings) {
        checkKey("id", settings);
        return (String)settings.get("id");
    }

    private static String url(Map settings) {
        if (!settings.containsKey("url")) {
            return "";
        }
        return (String)settings.get("url");
    }

    private static String description(Map settings) {
        checkKey("description", settings);
        return (String)settings.get("description");
    }

    private static boolean sliding(Map settings) {
        checkValues(settings, "sliding", "on", "off");
        return settings.get("sliding").equals("on");
    }

    private static boolean clockwise(Map settings) {
        checkValues(settings, "rotation", "clockwise", "anticlockwise");
        return settings.get("rotation").equals("clockwise");
    }

    private static PieceStartPos pieceStartPos(Map settings) {
        checkKey("piece start position on standard board", settings);

        List pos = (List)settings.get("piece start position on standard board");

        return new PieceStartPos(Integer.parseInt(pos.get(0).toString()), Integer.parseInt(pos.get(1).toString()));
    }

    private static Adjustments adjustments(String piece, Map settings) {
        checkKey(piece, settings);

        List<List> rotations = (List)settings.get(piece);

        List<AdjustmentDxDy> adjustments = new ArrayList<>();

        for (List<String> rotation : rotations) {
            int dx = Integer.parseInt(rotation.get(0));
            int dy = Integer.parseInt(rotation.get(1));
            adjustments.add(new AdjustmentDxDy(dx, dy));
        }
        return AdjustmentCalculator.calculate(piece, adjustments);
    }

    private static class PieceStartPos {
        public final int x;
        public final int y;

        private PieceStartPos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public CustomGameSettings(String id, String url, String description, int pieceStartX, int pieceStartY,
                               boolean sliding, boolean clockwise, Class clazz, Adjustments[] pieceAdjustments) {
        super(id, url, description, pieceStartX, pieceStartY, sliding, clockwise, clazz, pieceAdjustments);
    }
}
