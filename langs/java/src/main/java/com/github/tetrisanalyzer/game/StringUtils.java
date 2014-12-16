package com.github.tetrisanalyzer.game;

import java.util.Locale;

public class StringUtils {

    public static String format(long value) {
        return String.format(Locale.ENGLISH, "%,d", value).replace(',', ' ');
    }

    public static String format(double value) {
        if (value >= 1000) {
            return format((long)value);
        } else if (value >= 100) {
            return String.format(Locale.ENGLISH, "%.2f", value);
        } else if (value >= 10) {
            return String.format(Locale.ENGLISH, "%.3f", value);
        }
        return String.format(Locale.ENGLISH, "%.4f", value);
    }
}
