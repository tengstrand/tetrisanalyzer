package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

public class GameResult {
    public final Board board;
    public final ColoredBoard coloredBoard;
    public final PieceGenerator pieceGenerator;
    public long moves;
    public long movesLeft;
    public long games;
    public long rows;
    public long totalRows;
    public long cells;
    public final long[] cellDist;

    public GameResult(ColoredBoard coloredBoard, PieceGenerator pieceGenerator, int movesLeft) {
        this.board = coloredBoard.asBoard();
        this.pieceGenerator = pieceGenerator;
        cellDist = new long[(board.width - 1) * board.height + 1];
        this.movesLeft = movesLeft;
        this.coloredBoard = coloredBoard;
    }

    public String export() {
        long rowsPerGame = games == 0 ? 0 : totalRows / games;

        return "game result:" +
                board() +
                "\n  piece generator: " + pieceGenerator.state() +
                "\n  moves: " + format(moves) +
                "\n  rows: " + format(rows) +
                "\n  games: " + format(games) +
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
