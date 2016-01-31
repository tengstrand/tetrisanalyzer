package com.github.tetrisanalyzer.game;

public class ColumnStringConcater {

    public static String concat(String string1, String string2) {
        if (string1.isEmpty()) {
            return string2;
        }

        String[] strings1 = string1.split("\n");
        String[] strings2 = string2.split("\n");

        if (strings1.length != strings2.length) {
            throw new IllegalArgumentException("The strings must have the same number of rows. string1:  " + string1 + ", string2: " + string2);
        }
        int width = maxWidth(strings1);

        String result = "";

        String newline = "";
        for (int i=0; i<strings1.length; i++) {
            result += newline + rightPad(width, strings1[i]) + strings2[i];
            newline = "\n";
        }
        return result;
    }

    private static int maxWidth(String[] strings) {
        int max = Integer.MIN_VALUE;

        for (String string : strings) {
            if (string.length() > max) {
                max = string.length();
            }
        }
        return max;
    }

    private static String rightPad(int width, String string) {
        return string + new String(new char[width - string.length() + 1]).replace("\0", " ");
    }
}
