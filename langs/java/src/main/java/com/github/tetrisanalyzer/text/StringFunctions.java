package com.github.tetrisanalyzer.text;

public class StringFunctions {

    public static String spaces(int n) {
        return repeat(n, " ");
    }

    public static String repeat(int n, String character) {
        return new String(new char[n]).replace("\0", character);
    }
}
