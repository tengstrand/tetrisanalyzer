package com.github.tetrisanalyzer.game;

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
        String result = "Game-result {" +
                "\n  Board: [" + boardWidth + "," + boardHeight + "]" +
                "\n  Moves: " + moves +
                "\n  Moves-left: " + movesLeft +
                "\n  Games: " + games +
                "\n  Rows: " + rows +
                "\n  Dots: " + dots +
                "\n  Dots-distribution: [";

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
