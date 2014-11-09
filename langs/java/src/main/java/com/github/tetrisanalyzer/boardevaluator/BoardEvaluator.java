package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.List;
import java.util.Map;

import static com.github.tetrisanalyzer.settings.Setting.setting;

public abstract class BoardEvaluator {

    public abstract LessIs lessIs();
    public abstract double evaluate(Board board, AllValidPieceMoves allValidPieceMoves);
    public abstract BoardEvaluatorSettings settings();

    public abstract String id();
    public abstract String description();
    public abstract String author();
    public abstract String url();
    public abstract int boardWidth();
    public abstract int boardHeight();

    public abstract BoardEvaluator copy();

    public String export() {
        return new BoardEvaluatorSettings(
                setting("id", id()),
                setting("description", description()),
                setting("author", author()),
                setting("url", url()),
                setting("class", this.getClass().getCanonicalName()),
                setting("less is", lessIs().message)).add(settings()).export();
    }

    public static int boardWidth(Map settings) {
        return boardSize(settings, "board evaluator").get(0);
    }

    public static int boardHeight(Map settings) {
        return boardSize(settings, "board evaluator").get(1);
    }

    private static SettingsReader reader(Map settings, String group) {
        return new SettingsReader(settings, group);
    }

    private static List<Integer> boardSize(Map settings, String group) {
        return reader(settings, group).readIntegers("board");
    }

    @Override
    public String toString() {
        return export();
    }
}
