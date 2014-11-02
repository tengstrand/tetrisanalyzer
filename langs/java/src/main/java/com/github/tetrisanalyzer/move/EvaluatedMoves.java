package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.piecemove.PieceMove;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes a list of piece moves and evaluates them using the given board evaluator.
 */
public class EvaluatedMoves {
    final List<MoveEquity> moves;
    private final List<PieceMove> pieceMoves;
    private final BoardEvaluator boardEvaluator;

    public EvaluatedMoves(AllValidPieceMoves allValidPieceMoves, List<PieceMove> pieceMoves, BoardEvaluator boardEvaluator, Board board) {
        this.pieceMoves = pieceMoves;
        this.boardEvaluator = boardEvaluator;
        moves = evaluateValidMoves(board, allValidPieceMoves);
    }

    /**
     * Returns the move (if any) that is ranked as number one.
     */
    public PieceMove bestMove() {
        if (moves.isEmpty()) {
            return null;
        }
        double bestEquity = Double.MAX_VALUE;
        PieceMove bestPieceMove = null;

        for (MoveEquity move : moves) {
            if (move.equity < bestEquity) {
                bestEquity = move.equity;
                bestPieceMove = move.pieceMove;
            }
        }
        return bestPieceMove;
    }

    /**
     * Evaluates the equity of all valid moves for a given position.
     */
    private List<MoveEquity> evaluateValidMoves(Board board, AllValidPieceMoves allValidPieceMoves) {
        List<MoveEquity> evaluatedMoves = new ArrayList<MoveEquity>();
        if (!pieceMoves.isEmpty()) {
            Board boardCopy = board.copy();

            for (PieceMove pieceMove : pieceMoves) {
                evaluatedMoves.add(new MoveEquity(pieceMove, evaluate(pieceMove, board, boardCopy, allValidPieceMoves)));
            }
        }
        return evaluatedMoves;
    }

    private double evaluate(PieceMove pieceMove, Board board, Board boardCopy, AllValidPieceMoves allValidPieceMoves) {
        int clearedRows = pieceMove.setPiece(board);
        double equity = boardEvaluator.evaluate(board, allValidPieceMoves);

        if (clearedRows == 0) {
            pieceMove.clearPiece(board);
        } else {
            board.restore(boardCopy);
        }
        return equity;
    }
}
