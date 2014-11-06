package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.ClockwiseRotation;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomGameSettings extends GameSettings {
    private final String id;
    private final RotationDirection rotationDirection;
    private final String description;
    public final Class clazz;

    public static CustomGameSettings fromMap(Map settings) {
        String id = id(settings);
        String description = description(settings);
        boolean sliding = sliding(settings);
        boolean clockwise = clockwise(settings);
        PieceStartPos pieceStartPos = pieceStartPos(settings);
        Class clazz = classname(settings);

        Adjustments O = adjustments("O", settings);
        Adjustments I = adjustments("I", settings);
        Adjustments S = adjustments("S", settings);
        Adjustments Z = adjustments("Z", settings);
        Adjustments L = adjustments("L", settings);
        Adjustments J = adjustments("J", settings);
        Adjustments T = adjustments("T", settings);

        Adjustments[] pieceAdjustments = new Adjustments[] { null, O, I, S, Z, L, J, T };

        return new CustomGameSettings(id, description, pieceStartPos.x, pieceStartPos.y, sliding, clockwise, clazz, pieceAdjustments);
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

    private static Class classname(Map settings) {
        if (!settings.containsKey("class")) {
            return null;
        }
        try {
            return Class.forName((String)settings.get("class"));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String description(Map settings) {
        checkKey("description", settings);
        return (String)settings.get("description");
    }

    private static String id(Map settings) {
        checkKey("id", settings);
        return (String)settings.get("id");
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

    private static void checkKey(String key, Map settings) {
        if (!settings.containsKey(key)) {
            throw new IllegalArgumentException("Expected attribute '" + key + "' in game rules");
        }
    }

    private static void checkValues(Map settings, String key, String... validValues) {
        checkKey(key, settings);
        Object value = settings.get(key);

        for (String validValue : validValues) {
            if (value.equals(validValue)) {
                return;
            }
        }
        throw new IllegalArgumentException("Valid values for key '" + key + "' are: " + Arrays.asList(validValues) + ", but was: " + value);
    }

    private static class PieceStartPos {
        public final int x;
        public final int y;

        private PieceStartPos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private CustomGameSettings(String id, String description, int pieceStartX, int pieceStartY,
                               boolean slidingEnabled, boolean clockwise, Class clazz, Adjustments[] pieceAdjustments) {
        super(pieceStartX, pieceStartY, slidingEnabled, clazz, pieceAdjustments);

        this.id = id;
        this.description = description;
        rotationDirection = clockwise ? new ClockwiseRotation() : new AnticlockwiseRotation();
        this.clazz = clazz;
    }

    @Override public String id() { return id; }
    @Override public String url() { return null; }
    @Override public String description() { return description; }
    @Override public RotationDirection rotationDirection() { return rotationDirection; }
}
