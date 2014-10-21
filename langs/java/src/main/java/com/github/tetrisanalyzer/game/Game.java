package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.move.EvaluatedMoves;
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
        this.coloredBoard = gameResult.coloredBoard.copy();
        this.result = gameResult;
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = result.pieceGenerator;
        this.settings = settings;
        this.cells = board.numberOfFilledCells();

        allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
    }

    /**
     * Plays a specified number of pieces (result.movesLeft).
     */
    public void play() {
        result.startTime = Duration.currentTime();

        while (result.movesLeft > 0) {
            Piece piece = pieceGenerator.nextPiece();
            PieceMove bestMove = evaluateBestMove(piece);
            result.moves++;
            result.movesLeft--;
            //System.out.println("\n" + coloredBoard.asString(piece, bestMove.move));
            int clearedRows = bestMove.setPiece();
            coloredBoard.setPiece(bestMove.piece, bestMove.move);
            result.rows += clearedRows;
            cells += 4 - clearedRows * board.width;
            result.cells += cells;
            result.cellDist[cells]++;
        }
        result.endTime = Duration.currentTime();
    }

    private PieceMove evaluateBestMove(Piece piece) {
        PieceMove bestMove = evaluatePiece(piece);

        if (bestMove == null) {
            result.games++;
            result.totalRows += result.rows;
            result.rows = 0;
            board = result.board.copy();
            coloredBoard = result.coloredBoard.copy();
            cells = board.numberOfFilledCells();
            allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
            bestMove = evaluatePiece(piece);
            if (bestMove == null) {
                throw new IllegalStateException("The starting position is occupied!");
            }
        }
        return bestMove;
    }

    private PieceMove evaluatePiece(Piece piece) {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(piece);
        List<PieceMove> validMoves = new ValidMoves(board).pieceMoves(startPieceMove);
        return new EvaluatedMoves(validMoves, boardEvaluator).bestMove();
    }
}
