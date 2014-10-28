package com.github.tetrisanalyzer.boardevaluator;

public enum LessIs {
    BETTER("better"), WORSE("worse");

    public String message;

    LessIs(String message) {
        this.message = message;
    }
}
