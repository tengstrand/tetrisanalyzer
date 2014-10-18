package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;

public class GameResult {
    public Board board;
    public ColoredBoard coloredBoard;
    public long moves;
    public long movesLeft;
    public long games;
    public long rows;
    public long totalRows;
    public long dots;
    public long[] dotDist;

    public GameResult(ColoredBoard coloredBoard, int movesLeft) {
        this.board = coloredBoard.asBoard();
        dotDist = new long[(board.width - 1) * board.height + 1];
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
                "\n  dots total: " + format(dots) +
                "\n  dots distribution: [" + dots() + "]\n";
    }

    private String board() {
        String result = "\n  board size: [" + board.width + "," + board.height + "]";
        if (!board.isBoardEmpty()) {
            result += coloredBoard.export("start board", "    ");
        }
        return result + "\n";
    }

    private String dots() {
        String result = "";
        String separator = "";

        int dotstep = 2 - (board.width & 1);

        for (int i = 0; i<dotDist.length; i+=dotstep) {
            result += separator + dotDist[i];
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
