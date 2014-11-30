package com.github.tetrisanalyzer.settings;

import java.awt.*;

public class ColorConverter {

    public static Color color(String rgb) {
        int r = Integer.parseInt(rgb.substring(0, 2), 16);
        int g = Integer.parseInt(rgb.substring(2,4), 16);
        int b = Integer.parseInt(rgb.substring(4,6), 16);

        return new Color(r, g, b);
    }
}
