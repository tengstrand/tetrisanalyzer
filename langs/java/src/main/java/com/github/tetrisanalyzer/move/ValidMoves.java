package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.PieceMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidMoves {
    private List<PieceMove> validMoves = new ArrayList<PieceMove>();
    private int[][] visitedMoves;

    public ValidMoves(Board board) {
        visitedMoves = getEmptyVisitedMoves(board.getHeight(), board.getWidth());
    }

    private int[][] getEmptyVisitedMoves(int height, int width) {
        int[][] visitedMoves = new int[height][];
        for (int h=0; h<height; h++) {
            visitedMoves[h] = new int[width];
            Arrays.fill(visitedMoves[h], 0);
        }
        return visitedMoves;
    }

    private void markAsVisited(Move move) {
        visitedMoves[move.getY()][move.getX()] |= 1 << move.getRotation();
    }

    private boolean isUnvisited(Move move) {
        try {
            return (visitedMoves[move.getY()][move.getX()] & (1 << move.getRotation())) == 0;
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * The incoming pieceMove is at the starting position of the board, and
     * links to all possible moves on an empty board. Because the board is probably not empty,
     * every possible move needs to be checked (and returned by this method).
     */
    public List<PieceMove> getPieceMoves(PieceMove pieceMove) {
        if (isUnvisited(pieceMove.getMove())) {
            markAsVisited(pieceMove.getMove());

            for (PieceMove move: pieceMove.getFreeAsideAndRotateMoves()) {
                getPieceMoves(move);
            }
            if (pieceMove.canMoveDown()) {
                getPieceMoves(pieceMove.getDown());
            } else {
                validMoves.add(pieceMove);
            }
        }
        return validMoves;
    }
}
