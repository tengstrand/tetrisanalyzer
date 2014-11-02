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
    public final CellDistribution cellDist;

    public GameState(ColoredBoard coloredBoard, PieceGenerator pieceGenerator, int movesLeft) {
        this(coloredBoard.asBoard(), pieceGenerator, movesLeft);
        this.coloredBoard = coloredBoard;
    }

    public GameState(Board board, PieceGenerator pieceGenerator, int movesLeft) {
        this.board = board;
        this.pieceGenerator = pieceGenerator;
        cellDist = new CellDistribution(board.width, board.height);
        this.movesLeft = movesLeft;
        this.nonstop = movesLeft <= 0;
    }

    public GameState copy() {
        return new GameState(duration, board, coloredBoard, pieceGenerator, moves, nonstop, movesLeft, games, rows, totalRows, cellDist);
    }

    private GameState(Duration duration, Board board, ColoredBoard coloredBoard, PieceGenerator pieceGenerator,
                      long moves, boolean nonstop, long movesLeft, long games, long rows, long totalRows, CellDistribution cellDist) {
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
        this.cellDist = new CellDistribution(board.width, board.height);
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
                "\n  cell step: " + cellDist.step +
                "\n  filled numberOfcells total: " + format(cellDist.numberOfcells) +
                "\n  filled distribution distribution: [" + cellDist.distribution() + "]\n" +
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

    @Override
    public String toString() {
        return export();
    }
}
