
package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import static com.github.tetrisanalyzer.game.StringUtils.format;

public class GameState {
    public String id;
    public Duration duration;
    public final Board board;
    public ColoredBoard coloredBoard;
    public final PieceGenerator pieceGenerator;
    public long moves;
    public boolean nonstop;
    public long movesLeft;
    public long games;
    public long rows;
    public long minRows = Long.MAX_VALUE;
    public long maxRows = Long.MIN_VALUE;
    public long totalRows;
    public long numberOfCells;

    public GameState(String id, ColoredBoard coloredBoard, PieceGenerator pieceGenerator, int movesLeft) {
        this(id, coloredBoard.asBoard(), pieceGenerator, movesLeft);
        this.coloredBoard = coloredBoard;
    }

    public GameState(String id, Board board, PieceGenerator pieceGenerator, int movesLeft) {
        this.id = id;
        this.board = board;
        this.pieceGenerator = pieceGenerator;
        this.movesLeft = movesLeft;
        this.nonstop = movesLeft <= 0;
    }

    public String minRows() {
        return minRows == Long.MAX_VALUE ? "" : format(minRows);
    }

    public String maxRows() {
        return maxRows == Long.MIN_VALUE ? "" : format(maxRows);
    }

    public GameState copy() {
        return new GameState(duration, board, coloredBoard, pieceGenerator, moves, nonstop, movesLeft, games, rows,
                minRows, maxRows, totalRows, numberOfCells);
    }

    private GameState(Duration duration, Board board, ColoredBoard coloredBoard, PieceGenerator pieceGenerator,
                      long moves, boolean nonstop, long movesLeft, long games, long rows, long minRows, long maxRows,
                      long totalRows, long numberOfCells) {
        this.duration = duration;
        this.board = board.copy();
        this.coloredBoard = coloredBoard == null ? null : coloredBoard.copy();
        this.pieceGenerator = pieceGenerator.copy();
        this.moves = moves;
        this.nonstop = nonstop;
        this.movesLeft = movesLeft;
        this.games = games;
        this.rows = rows;
        this.minRows = minRows;
        this.maxRows = maxRows;
        this.totalRows = totalRows;
        this.numberOfCells = numberOfCells;
    }

    public String rowsPerGame() {
        long rowsPerGame = games == 0 ? 0 : totalRows / games;
        return format(rowsPerGame);
    }

    public String export() {
        return "game state:" +
                "  id: " + id + "\n" +
                board() +
                "\n  seed: " + pieceGenerator.state() +
                "\n  duration: " + duration +
                "\n  pieces: " + format(moves) +
                "\n  rows: " + format(rows) +
                "\n  games: " + format(games) +
                "\n  rows (finished games): " + format(totalRows) +
                "\n  cell step: " + cellStep() +
                "\n  filled cells total: " + format(numberOfCells) +
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

    @Override
    public String toString() {
        return export();
    }
}
