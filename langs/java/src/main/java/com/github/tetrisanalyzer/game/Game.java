package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.move.EvaluatedMoves;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.ValidMoves;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMovesForEmptyBoard;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.GameSettings;

import java.util.List;

/**
 * Plays a game of Tetris using specified board, board evaluator, piece generator and settings.
 */
public class Game {
    private AllValidPieceMovesForEmptyBoard allValidPieceMoves;

    private int cells;
    public Board board;
    public ColoredBoard coloredBoard;
    private final BoardEvaluator boardEvaluator;
    public final PieceGenerator pieceGenerator;
    private final GameSettings settings;
    private final GameResult result;

    public Game(GameResult gameResult, BoardEvaluator boardEvaluator, GameSettings settings) {
        this.board = gameResult.board.copy();
        if (gameResult.coloredBoard != null) {
            this.coloredBoard = gameResult.coloredBoard.copy();
        }
        this.result = gameResult;
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = result.pieceGenerator;
        this.settings = settings;
        this.cells = board.numberOfOccupiedCells();

        allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
    }

    private String board(long moveNo, Piece piece, Move move) {
        if (coloredBoard != null) {
            return coloredBoard.asString(moveNo, piece, move);
        }
        return board.asString(moveNo, piece, move);
    }

    /**
     * Plays a specified number of pieces (result.movesLeft).
     */
    public void play() {
        long startTime = Duration.currentTime();
        PieceMove bestMove = null;

        while (result.movesLeft > 0) {
            Piece piece = pieceGenerator.nextPiece();
            bestMove = evaluateBestMove(piece);
            printBoardEvery10000Piece(result.moves + 1, piece, bestMove.move);
            result.moves++;
            result.movesLeft--;

            int clearedRows = bestMove.setPiece();
            setPieceOnColoredBoard(bestMove.piece, bestMove.move);
            result.rows += clearedRows;
            cells += 4 - clearedRows * board.width;
            result.cells += cells;
            result.cellDist[cells]++;
        }
        result.duration = Duration.create(startTime);

        if (bestMove != null) {
            System.out.println("\n" + board(result.moves + 1, pieceGenerator.copy().nextPiece(), bestMove.move));
        }
        System.out.println();
    }

    private PieceMove evaluateBestMove(Piece piece) {
        PieceMove bestMove = evaluatePiece(piece);

        if (bestMove == null) {
            result.games++;
            result.totalRows += result.rows;
            result.rows = 0;
            board = result.board.copy();
            initColoredBoard();
            cells = board.numberOfOccupiedCells();
            allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
            bestMove = evaluatePiece(piece);
            if (bestMove == null) {
                throw new IllegalStateException("The starting position is occupied!");
            }
        }
        return bestMove;
    }

    private void printBoardEvery10000Piece(long moveNo, Piece piece, Move move) {
        if ((result.moves % 10000) == 0) {
            System.out.println("\n" + board(moveNo, piece, move));
        }
    }

    private void setPieceOnColoredBoard(Piece piece, Move move) {
        if (coloredBoard != null) {
            coloredBoard.setPiece(piece, move);
        }
    }

    private void initColoredBoard() {
        if (coloredBoard != null) {
            coloredBoard = result.coloredBoard.copy();
        }
    }

    private PieceMove evaluatePiece(Piece piece) {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(piece);
        List<PieceMove> validMoves = new ValidMoves(board).pieceMoves(startPieceMove);
        return new EvaluatedMoves(validMoves, boardEvaluator).bestMove();
    }
}
