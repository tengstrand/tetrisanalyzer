package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import static com.github.tetrisanalyzer.game.StringUtils.format;

public class GameResult {
    public Duration duration;
    public final Board board;
    public ColoredBoard coloredBoard;
    public final PieceGenerator pieceGenerator;
    public long moves;
    public long movesLeft;
    public long games;
    public long rows;
    public long totalRows;
    public long cells;
    public final long[] cellDist;

    public GameResult(ColoredBoard coloredBoard, PieceGenerator pieceGenerator, int movesLeft) {
        this(coloredBoard.asBoard(), pieceGenerator, movesLeft);
        this.coloredBoard = coloredBoard;
    }

    public GameResult(Board board, PieceGenerator pieceGenerator, int movesLeft) {
        this.board = board;
        this.pieceGenerator = pieceGenerator;
        cellDist = new long[(board.width - 1) * board.height + 1];
        this.movesLeft = movesLeft;
    }

    public String export() {
        long rowsPerGame = games == 0 ? 0 : totalRows / games;

        return "game result:" +
                board() +
                "\n  seed: " + pieceGenerator.state() +
                "\n  duration: " + duration +
                "\n  pieces/s: " + duration.xPerSeconds(moves) +
                "\n  pieces: " + format(moves) +
                "\n  rows: " + format(rows) +
                "\n  games: " + format(games) +
                "\n  rows (finished games): " + format(totalRows) +
                "\n  rows/game: " + format(rowsPerGame) +
                "\n  cell step: " + cellStep() +
                "\n  filled cells total: " + format(cells) +
                "\n  filled cells distribution: [" + cells() + "]\n";
    }
    private String board() {
        String result = "\n  board size: [" + board.width + "," + board.height + "]";
        if (!board.isBoardEmpty()) {
            if (coloredBoard == null) {
                result += board.export("start board", "    ");
            } else {
                result += coloredBoard.export("start board", "    ");
            }
        }
        return result;
    }

    private int cellStep() {
        return 2 - (board.width & 1);
    }

    private String cells() {
        String result = "";
        String separator = "";

        int step = cellStep();

        for (int i = 0; i< cellDist.length; i+=step) {
            result += separator + cellDist[i];
            separator = ",";
        }
        return result;
    }

    @Override
    public String toString() {
        return export();
    }
}
