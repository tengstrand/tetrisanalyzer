package com.github.tetrisanalyzer.boardevaluator;

public enum EvaluationType {
    LESS_IS_BETTER("less is better"), GREATER_IS_BETTER("greater is better");

    public String message;

    EvaluationType(String message) {
        this.message = message;
    }
}
