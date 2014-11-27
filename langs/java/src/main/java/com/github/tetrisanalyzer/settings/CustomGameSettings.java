package com.github.tetrisanalyzer.settings;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator;
import com.github.tetrisanalyzer.settings.adjustment.AdjustmentDxDy;
import com.github.tetrisanalyzer.settings.adjustment.Adjustments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.tetrisanalyzer.settings.adjustment.AdjustmentCalculator.calculate;

public class CustomGameSettings extends GameSettings {

    public static CustomGameSettings fromString(String settings) {
        try {
            return fromMap((Map)new YamlReader(settings).read());
        } catch (YamlException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static CustomGameSettings fromMap(Map settings) {
        SettingsReader reader = new SettingsReader(settings, "game rule");

        String id = reader.readString("id");
        String url = reader.readString("url");
        String description = reader.readString("description");
        boolean sliding = reader.readString("sliding", "on", "off").equals("on");
        boolean clockwise = reader.readString("rotation", "clockwise", "anticlockwise").equals("clockwise");

        List<Integer> pos = reader.readIntegers("piece start position on standard board", 2);
        int pieceStartXOnStandardBoard = pos.get(0);
        int pieceStartY = pos.get(1);

        Class clazz = reader.readClass("class");

        Adjustments empty = calculate("-", dxdy(0,0));
        Adjustments O = adjustments(reader, "O");
        Adjustments I = adjustments(reader, "I");
        Adjustments S = adjustments(reader, "S");
        Adjustments Z = adjustments(reader, "Z");
        Adjustments L = adjustments(reader, "L");
        Adjustments J = adjustments(reader, "J");
        Adjustments T = adjustments(reader, "T");
        Adjustments any = calculate("-", dxdy(0,0));
        Adjustments shadow = calculate("-", dxdy(0,0));

        Adjustments[] pieceAdjustments = new Adjustments[] { empty, O, I, S, Z, L, J, T, any, shadow };

        return new CustomGameSettings(id, url, description, pieceStartXOnStandardBoard, pieceStartY, sliding, clockwise, clazz, pieceAdjustments);
    }

    private static Adjustments adjustments(SettingsReader reader, String piece) {
        List<List> rotations = reader.readLists(piece);

        List<AdjustmentDxDy> adjustments = new ArrayList<>();

        for (List<String> rotation : rotations) {
            reader.ensureSize(rotation.get(0), rotation, 2);
            int dx = Integer.parseInt(rotation.get(0));
            int dy = Integer.parseInt(rotation.get(1));
            adjustments.add(new AdjustmentDxDy(dx, dy));
        }
        return AdjustmentCalculator.calculate(piece, adjustments);
    }

    public CustomGameSettings(String id, String url, String description, int pieceStartXOnStandardBoard, int pieceStartY,
                               boolean sliding, boolean clockwise, Class clazz, Adjustments[] pieceAdjustments) {
        super(id, url, description, pieceStartXOnStandardBoard, pieceStartY, sliding, clockwise, clazz, pieceAdjustments);
    }
}
