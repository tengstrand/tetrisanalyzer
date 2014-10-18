package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.move.EvaluatedMoves;
import com.github.tetrisanalyzer.move.ValidMoves;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMovesForEmptyBoard;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.GameSettings;

import java.util.List;

/**
 * Plays a game of Tetris using specified board, board evaluator and settings.
 */
public class Game {
    private AllValidPieceMovesForEmptyBoard allValidPieceMoves;

    private int dots;
    public Board board;
    private BoardEvaluator boardEvaluator;
    public PieceGenerator pieceGenerator;
    private GameSettings settings;
    private GameResult result;

    public Game(GameResult gameResult, BoardEvaluator boardEvaluator, PieceGenerator pieceGenerator, GameSettings settings) {
        this.board = new Board(gameResult.board);
        this.result = gameResult;
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = pieceGenerator;
        this.settings = settings;
        this.dots = board.numberOfDots();

        allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
    }

    /**
     * Play specified number of pieces (result.movesLeft).
     */
    public void play() {
        while (result.movesLeft > 0) {
            PieceMove bestMove = evaluateBestMove();
            result.moves++;
            result.movesLeft--;
            int clearedRows = bestMove.setPiece();
            result.rows += clearedRows;
            dots += 4 - clearedRows * board.width;
            result.dots += dots;

            result.dotDist[dots]++;
        }
    }

    private PieceMove evaluateBestMove() {
        PieceMove bestMove = evaluateNextPiece();

        if (bestMove == null) {
            result.games++;
            result.totalRows += result.rows;
            result.rows = 0;
            board = new Board(result.board);
            dots = board.numberOfDots();
            allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
            bestMove = evaluateNextPiece();
            if (bestMove == null) {
                throw new IllegalStateException("The starting position is occupied!");
            }
        }
        return bestMove;
    }

    private PieceMove evaluateNextPiece() {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(pieceGenerator.nextPiece());
        List<PieceMove> validMoves = new ValidMoves(board).getPieceMoves(startPieceMove);
        return new EvaluatedMoves(validMoves, boardEvaluator).bestMove();
    }
}
