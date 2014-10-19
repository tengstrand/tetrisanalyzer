package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;

import static com.github.tetrisanalyzer.settings.Setting.setting;

public abstract class BoardEvaluator {

    public abstract EvaluationType evaluationType();
    public abstract double evaluate(Board board);
    public abstract BoardEvaluatorSettings settings();

    public abstract String description();
    public abstract String author();
    public abstract String url();
    public abstract int minBoardX();
    public abstract int maxBoardX();
    public abstract int minBoardY();
    public abstract int maxBoardY();

    public String export() {
        return new BoardEvaluatorSettings(
                setting("description", description()),
                setting("author", author()),
                setting("url", url()),
                setting("class", this.getClass().getCanonicalName()),
                setting("evaluation type", evaluationType().message),
                setting("min board size", "[" + minBoardX() + "," + minBoardY() + "]"),
                setting("max board size", "[" + maxBoardX() + "," + maxBoardY() + "]")).add(settings()).export();
    }

    @Override
    public String toString() {
        return export();
    }
}
