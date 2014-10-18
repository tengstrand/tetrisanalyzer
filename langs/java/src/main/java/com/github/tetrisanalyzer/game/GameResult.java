package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;

public class GameResult {
    public Board board;
    public long moves;
    public long movesLeft;
    public long games;
    public long rows;
    public long totalRows;
    public long dots;
    public long[] dotDist;

    public GameResult(Board board, int movesLeft) {
        dotDist = new long[(board.width - 1) * board.height + 1];
        this.movesLeft = movesLeft;
        this.board = new Board(board);
    }

    public String export() {
        long rowsPerGame = games == 0 ? 0 : (rows + totalRows) / games;


        return "game result:" +
                board() +
                "\n  moves: " + format(moves) +
                "\n  games: " + format(games) +
                "\n  rows: " + format(rows) +
                "\n  rows total: " + format(totalRows) +
                "\n  rows/game: " + format(rowsPerGame) +
                "\n  dots total: " + format(dots) +
                "\n  dots distribution: [" + dots() + "]\n";
    }

    private String board() {
        String result = "\n  board size: [" + board.width + "," + board.height + "]";
        if (!board.isBoardEmpty()) {
            result += board.export("start board", "   ");
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
