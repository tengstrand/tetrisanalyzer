package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
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

    public EvaluatedMoves(List<PieceMove> pieceMoves, BoardEvaluator boardEvaluator) {
        this.pieceMoves = pieceMoves;
        this.boardEvaluator = boardEvaluator;
        moves = evaluateValidMoves();
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
    private List<MoveEquity> evaluateValidMoves() {
        List<MoveEquity> evaluatedMoves = new ArrayList<MoveEquity>();
        if (!pieceMoves.isEmpty()) {
            Board boardCopy = pieceMoves.get(0).copyBoard();

            for (PieceMove pieceMove : pieceMoves) {
                evaluatedMoves.add(new MoveEquity(pieceMove, evaluate(pieceMove, boardCopy)));
            }
        }
        return evaluatedMoves;
    }

    private double evaluate(PieceMove pieceMove, Board boardCopy) {
        int clearedLines = pieceMove.setPiece();
        double equity = boardEvaluator.evaluate(pieceMove.board);

        if (clearedLines == 0) {
            pieceMove.clearPiece();
        } else {
            pieceMove.board.restore(boardCopy);
        }
        return equity;
    }
}
