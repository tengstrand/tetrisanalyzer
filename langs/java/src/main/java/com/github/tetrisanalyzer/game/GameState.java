
package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import static com.github.tetrisanalyzer.game.StringUtils.format;

public class GameState {
    public Duration duration;
    public final Board board;
    public ColoredBoard coloredBoard;
    public final BoardEvaluator boardEvaluator;
    public final PieceGenerator pieceGenerator;
    public long pieces;
    public long totalPieces;
    public boolean nonstop;
    public long movesLeft;
    public long games;
    public long rows;
    public long minRows;
    public long maxRows;
    public long totalRows;
    public long numberOfCells;

    public GameState(Duration duration, ColoredBoard coloredBoard, BoardEvaluator boardEvaluator,
                     PieceGenerator pieceGenerator, long numberOfGames, long numberOfPieces,
                     long totalNumberOfPieces, long totalNumberOfRows, long minRows, long maxRows,
                     long piecesLeft) {
        //this(duration, coloredBoard.asBoard(), boardEvaluator, pieceGenerator, movesLeft);
        this.duration = duration;
        this.coloredBoard = coloredBoard;
        this.board = coloredBoard.asBoard();
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = pieceGenerator;
        this.games = numberOfGames;
        this.totalPieces = totalNumberOfPieces;
        this.totalRows = totalNumberOfRows;
        this.minRows = minRows;
        this.maxRows = maxRows;
        this.movesLeft = piecesLeft;
        this.nonstop = piecesLeft <= 0;
    }

    public String piecesPerSecond() {
        return duration.xPerSeconds(totalPieces);
    }

    public String minRows() {
        return minRows == Long.MAX_VALUE ? "" : format(minRows);
    }

    public String maxRows() {
        return maxRows == Long.MIN_VALUE ? "" : format(maxRows);
    }

    public GameState copy() {
        return new GameState(duration, board, coloredBoard, boardEvaluator, pieceGenerator,
                totalPieces, nonstop, movesLeft, games, rows, minRows, maxRows, totalRows, numberOfCells);
    }

    private GameState(Duration duration, Board board,
                      ColoredBoard coloredBoard, BoardEvaluator boardEvaluator, PieceGenerator pieceGenerator,
                      long totalPieces, boolean nonstop, long movesLeft, long games, long rows, long minRows, long maxRows,
                      long totalRows, long numberOfCells) {
        this.duration = duration;
        this.board = board.copy();
        this.coloredBoard = coloredBoard == null ? null : coloredBoard.copy();
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = pieceGenerator.copy();
        this.totalPieces = totalPieces;
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
        return "GameState{" +
                "\nrows=" + rows +
                "\ntotalPieces=" + totalPieces +
                "\ngames=" + games +
                "\nminRows=" + minRows +
                "\nmaxRows=" + maxRows +
                "\ntotalRows=" + totalRows +
                '}';
    }
}
