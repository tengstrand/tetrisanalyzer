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
    int dots;
    public int rows;
    private AllValidPieceMovesForEmptyBoard allValidPieceMoves;

    public Board board;
    private BoardEvaluator boardEvaluator;
    private PieceGenerator pieceGenerator;
    private GameSettings settings;
    private GameResult gameResult;

    public Game(GameResult gameResult, BoardEvaluator boardEvaluator, PieceGenerator pieceGenerator, GameSettings settings) {
        this.board = new Board(gameResult.board);
        this.gameResult = gameResult;
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
        while (gameResult.movesLeft > 0) {
            PieceMove bestMove = evaluateBestMove(gameResult);
            gameResult.moves++;
            gameResult.movesLeft--;
            int clearedRows = bestMove.setPiece();
            rows += clearedRows;
            dots += 4 - clearedRows * board.width;
            gameResult.dots += dots;

            gameResult.dotDist[dots]++;
        }
    }

    private PieceMove evaluateBestMove(GameResult result) {
        PieceMove bestMove = evaluateNextPiece();

        if (bestMove == null) {
//            System.out.println(board + "\n");

            result.games++;
            result.totalRows += rows;
            rows = 0;
            result.rows = 0;
            board = new Board(gameResult.board);
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
