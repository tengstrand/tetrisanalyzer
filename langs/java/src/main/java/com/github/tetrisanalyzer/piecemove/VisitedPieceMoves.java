package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for the calculation of valid moves on an empty board.
 */
public class VisitedPieceMoves {
    private Board board;
    private Piece piece;
    private int[][][] moves;
    private Map<Move, PieceMove> validMoves = new HashMap<Move, PieceMove>();

    public VisitedPieceMoves(Board board, Piece piece) {
        this.board = board;
        this.piece = piece;

        moves = getEmptyMoves(board.getHeight(), board.getWidth(), Direction.NUMBER_OF_DIRECTIONS);
    }

    private int[][][] getEmptyMoves(int height, int width, int directions) {
        int[][][] moves = new int[height][][];

        for (int h=0; h<height; h++) {
            moves[h] = new int[width][];
            for (int w=0; w<width; w++) {
                moves[h][w] = new int[directions];
                Arrays.fill(moves[h][w], 0);
            }
        }
        return moves;
    }

    /**
     * Returns an existing piece move or creates a new.
     */
    public PieceMove get(Move move) {
        if (validMoves.containsKey(move)) {
            return validMoves.get(move);
        } else {
            return new PieceMove(board, piece, move);
        }
    }

    /**
     * Marks a move as visited. The attribute 'movement.direction' says the manner in which the
     * transfer to this location has occurred. We also add the direction 'Rotate' because
     * we don't need to rotate the piece a full circle (to the position we just have).
     */
    public void visit(Movement movement) {
        Move move = movement.getMove();
        moves[move.y][move.x][movement.getDirectionIndex()] |= (1 << move.rotation);
        moves[move.y][move.x][Direction.ROTATE.getIndex()] |= (1 << move.rotation);
        validMoves.put(move, movement.getPieceMove());
    }

    /**
     * True if this movement has not been visited.
     */
    public boolean isUnvisited(Movement movement) {
        try {
            Move move = movement.getMove();
            return (moves[move.y][move.x][movement.getDirectionIndex()] & (1 << move.rotation)) == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
