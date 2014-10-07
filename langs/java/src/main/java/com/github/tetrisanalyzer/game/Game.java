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
    private int clearedLines = 0;
    private AllValidPieceMovesForEmptyBoard allValidPieceMoves;

    private Board board;
    private BoardEvaluator boardEvaluator;
    private PieceGenerator pieceGenerator;

    public Game(Board board, BoardEvaluator boardEvaluator, PieceGenerator pieceGenerator, GameSettings settings) {
        this.board = board;
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = pieceGenerator;

        allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);
    }

    public int getClearedLines() {
        return clearedLines;
    }

    /**
     * Plays the specified number of pieces.
     */
    public void play(long maxMoves) {
        long moves = 0;
        PieceMove bestMove = evaluateBestMove();

        while (moves < maxMoves && bestMove != null) {
            moves++;
            clearedLines += bestMove.setPiece();
            bestMove = evaluateBestMove();
        }
    }

    private PieceMove evaluateBestMove() {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(pieceGenerator.nextPiece());
        List<PieceMove> validMoves = new ValidMoves(board).getPieceMoves(startPieceMove);
        return new EvaluatedMoves(validMoves, boardEvaluator).bestMove();
    }
}

