package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import static com.github.tetrisanalyzer.game.StringUtils.format;

public class GameState {
    public Duration duration;
    public final Board board;
    public ColoredBoard coloredBoard;
    public final PieceGenerator pieceGenerator;
    public long moves;
    public boolean nonstop;
    public long movesLeft;
    public long games;
    public long rows;
    public long totalRows;
    public long cells;
    public final long[] cellDist;

    public GameState(ColoredBoard coloredBoard, PieceGenerator pieceGenerator, int movesLeft) {
        this(coloredBoard.asBoard(), pieceGenerator, movesLeft);
        this.coloredBoard = coloredBoard;
    }

    public GameState(Board board, PieceGenerator pieceGenerator, int movesLeft) {
        this.board = board;
        this.pieceGenerator = pieceGenerator;
        cellDist = new long[(board.width - 1) * board.height + 1];
        this.movesLeft = movesLeft;
        this.nonstop = movesLeft <= 0;
    }

    public GameState copy() {
        return new GameState(duration, board, coloredBoard, pieceGenerator, moves, nonstop, movesLeft, games, rows, totalRows, cells, cellDist);
    }

    private GameState(Duration duration, Board board, ColoredBoard coloredBoard, PieceGenerator pieceGenerator,
                      long moves, boolean nonstop, long movesLeft, long games, long rows, long totalRows, long cells, long[] cellDist) {
        this.duration = duration;
        this.board = board.copy();
        this.coloredBoard = coloredBoard == null ? null : coloredBoard.copy();
        this.pieceGenerator = pieceGenerator.copy();
        this.moves = moves;
        this.nonstop = nonstop;
        this.movesLeft = movesLeft;
        this.games = games;
        this.rows = rows;
        this.totalRows = totalRows;
        this.cells = cells;
        this.cellDist = new long[cellDist.length];
        System.arraycopy(this.cellDist, 0, cellDist, 0, cellDist.length);
    }

    public String rowsPerGame() {
        long rowsPerGame = games == 0 ? 0 : totalRows / games;
        return format(rowsPerGame);
    }

    public String export() {

        return "game state:" +
                board() +
                "\n  seed: " + pieceGenerator.state() +
                "\n  duration: " + duration +
                "\n  pieces: " + format(moves) +
                "\n  rows: " + format(rows) +
                "\n  games: " + format(games) +
                "\n  rows (finished games): " + format(totalRows) +
                "\n  cell step: " + cellStep() +
                "\n  filled cells total: " + format(cells) +
                "\n  filled cells distribution: [" + cells() + "]\n" +
                "\n  rows/game: " + rowsPerGame() +
                "\n  pieces/s: " + duration.xPerSeconds(moves);
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
