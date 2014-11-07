package com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

public class SettingsFunctions {

    public static void checkKey(String key, Map settings) {
        if (!settings.containsKey(key)) {
            throw new IllegalArgumentException("Expected attribute '" + key + "' in game rules");
        }
    }

    public static void checkValues(Map settings, String key, String... validValues) {
        checkKey(key, settings);
        Object value = settings.get(key);

        for (String validValue : validValues) {
            if (value.equals(validValue)) {
                return;
            }
        }
        throw new IllegalArgumentException("Valid values for key '" + key + "' are: " + Arrays.asList(validValues) + ", but was: " + value);
    }

    public static Class classname(Map settings) {
        if (!settings.containsKey("class")) {
            return null;
        }
        try {
            return Class.forName((String)settings.get("class"));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static boolean rotation(Map settings) {
        checkValues(settings, "rotation", "clockwise", "anticlockwise");
        return settings.get("rotation").equals("clockwise");
    }

    public static boolean sliding(Map settings) {
        checkValues(settings, "sliding", "on", "off");
        return settings.get("sliding").equals("on");
    }

    public static PieceGenerator createPieceGenerator(Class clazz, Map settings) {
        try {
            Constructor constructor = clazz.getConstructor(Map.class);
            return (PieceGenerator)constructor.newInstance(settings);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
