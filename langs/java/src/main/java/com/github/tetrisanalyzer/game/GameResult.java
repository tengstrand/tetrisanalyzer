package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;

public class GameResult {
    public final Board board;
    public final ColoredBoard coloredBoard;
    public long moves;
    public long movesLeft;
    public long games;
    public long rows;
    public long totalRows;
    public long cells;
    public final long[] cellDist;

    public GameResult(ColoredBoard coloredBoard, int movesLeft) {
        this.board = coloredBoard.asBoard();
        cellDist = new long[(board.width - 1) * board.height + 1];
        this.movesLeft = movesLeft;
        this.coloredBoard = coloredBoard;
    }

    public String export() {
        long rowsPerGame = games == 0 ? 0 : totalRows / games;

        return "game result:" +
                board() +
                "\n  moves: " + format(moves) +
                "\n  games: " + format(games) +
                "\n  rows current game: " + format(rows) +
                "\n  rows finished games: " + format(totalRows) +
                "\n  rows/game: " + format(rowsPerGame) +
                "\n  filled cells total: " + format(cells) +
                "\n  filled cells distribution: [" + cells() + "]\n";
    }

    private String board() {
        String result = "\n  board size: [" + board.width + "," + board.height + "]";
        if (!board.isBoardEmpty()) {
            result += coloredBoard.export("start board", "    ");
        }
        return result;
    }

    private String cells() {
        String result = "";
        String separator = "";

        int step = 2 - (board.width & 1);

        for (int i = 0; i< cellDist.length; i+=step) {
            result += separator + cellDist[i];
            separator = ",";
        }
        return result;
    }

    private String format(long value) {
        return String.format("%,d", value);
    }

    @Override
    public String toString() {
        return export();
    }
}
