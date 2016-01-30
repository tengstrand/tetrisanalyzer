package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.move.MoveEquity;
import com.github.tetrisanalyzer.move.ValidMoves;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.piecemove.PieceMove;

import java.util.ArrayList;
import java.util.List;

/**
 * Takes a list of piece moves and evaluates them using the given board evaluator.
 */
public class PositionEvaluator {

    public static MoveEquity bestMove(AllValidPieceMoves allValidPieceMoves, Piece[] pieces, BoardEvaluator boardEvaluator, Board board, NextPieces nextPieces) {
        return bestMove(evaluate(allValidPieceMoves, pieces, boardEvaluator, board, nextPieces));
    }

    public static List<MoveEquity> evaluate(AllValidPieceMoves allValidPieceMoves, Piece[] pieces, BoardEvaluator boardEvaluator, Board board, NextPieces nextPieces) {
        PieceMove startPieceMove = allValidPieceMoves.startMoveForPiece(nextPieces.piece());
        List<PieceMove> validMoves = new ValidMoves(board).pieceMoves(startPieceMove, board);

        List<MoveEquity> moves = new ArrayList<>(validMoves.size());

        for (PieceMove pieceMove : validMoves) {
            moves.add(new MoveEquity(pieceMove, evaluate(boardEvaluator, pieceMove, board, allValidPieceMoves, pieces, nextPieces.nextLevel(), boardEvaluator.maxEquity())));
        }
        return moves;
    }

    public static double evaluate(BoardEvaluator boardEvaluator, PieceMove pieceMove, Board board, AllValidPieceMoves allValidPieceMoves, Piece[] pieces, NextPieces nextPieces, double maxEquity) {
        Board boardCopy = board.copy();
        pieceMove.setPiece(boardCopy);

        if (nextPieces.level == 0) {
            return boardEvaluator.evaluate(boardCopy, allValidPieceMoves);
        }
        if (nextPieces.isUnknown()) {
            double equity = 0;
            for (Piece piece : pieces) {
                MoveEquity bestMove = bestMove(allValidPieceMoves, pieces, boardEvaluator, boardCopy, nextPieces.current(piece));
                if (bestMove == null) {
                    equity += maxEquity;
                } else {
                    equity += bestMove.equity;
                }
            }
            return equity / pieces.length;
        }

        MoveEquity bestMove = bestMove(allValidPieceMoves, pieces, boardEvaluator, boardCopy, nextPieces);

        return bestMove == null ? maxEquity : bestMove.equity;
    }

    /**
     * Returns the move (if any) that is ranked as number one.
     */
    public static MoveEquity bestMove(List<MoveEquity> moves) {
        if (moves.isEmpty()) {
            return null;
        }
        double bestEquity = Double.MAX_VALUE;
        MoveEquity bestMove = null;

        for (MoveEquity move : moves) {
            if (move.equity < bestEquity) {
                bestEquity = move.equity;
                bestMove = move;
            }
        }
        return bestMove;
    }
}
