package com.github.tetrisanalyzer.game;

import static java.lang.String.format;

public class GameResult {
    public int boardWidth;
    public int boardHeight;
    public long moves;
    public long movesLeft;
    public int games;
    public long rows;
    public long dots;
    public long[] dotDist;

    public GameResult(int boardWidth, int boardHeight, int movesLeft) {
        dotDist = new long[(boardWidth - 1) * boardHeight];
        this.movesLeft = movesLeft;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public double average() {
        return ((double) dots) / moves;
    }

    public String export() {
        String result = "Game result {" +
                "\n  Board: [" + boardWidth + "," + boardHeight + "]" +
                "\n  Moves: " + format("%,d", moves) +
                "\n  Moves left: " + format("%,d", movesLeft) +
                "\n  Games: " + format("%,d", games) +
                "\n  Rows: " + format("%,d", rows) +
                "\n  Dots: " + format("%,d", dots) +
                "\n  Distribution: [";

        String separator = "";
        int dotstep = 2 - (boardWidth & 1);

        for (int i = 0; i< dotDist.length; i+=dotstep) {
            result += separator + dotDist[i];
            separator = ",";
        }
        return result + "]\n}";
    }

    @Override
    public String toString() {
        return export();
    }
}
