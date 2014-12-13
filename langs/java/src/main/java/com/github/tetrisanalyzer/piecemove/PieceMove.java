package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds the valid moves of a piece + supports setting and clearing a piece.
 */
public class PieceMove {
    private int pieceHeight;
    public Piece piece;
    public final Move move;

    public PieceMove down;
    public Set<PieceMove> asideAndRotate = new HashSet<>();

    private int[] boardLineIndices;
    private long[] orLines;
    private long[] andLines;

    private static final long ALL_BITS_CLEARED = 0;
    private static final long ALL_BITS_SET = -1;

    public PieceMove(Piece piece, Move move) {
        this.piece = piece;
        this.move = move;

        pieceHeight = piece.height(move.rotation);
        orLines = new long[pieceHeight];
        andLines = new long[pieceHeight];
        Arrays.fill(orLines, ALL_BITS_CLEARED);
        Arrays.fill(andLines, ALL_BITS_SET);
        boardLineIndices = new int[pieceHeight];

        for (int y=0; y<pieceHeight; y++) {
            boardLineIndices[y] = move.y + y;
        }
        for (Point point: piece.getShape(move.rotation).getPoints()) {
            long bit = 1L << (move.x + point.x);
            orLines[point.y] |= bit;
            andLines[point.y] &= ~bit;
        }
    }

    public void setDown(PieceMove down) {
        this.down = down;
    }

    public void addPieceMove(PieceMove pieceMove) {
        asideAndRotate.add(pieceMove);
    }

    public Set<PieceMove> asideAndRotateMoves() {
        return asideAndRotate;
    }

    /**
     * Sets a piece on the board.
     *
     * @return number of cleared lines
     */
    public int setPiece(Board board) {
        for (int y=0; y<pieceHeight; y++) {
            board.setBits(boardLineIndices[y], orLines[y]);
        }
        return board.clearRows(move.y, pieceHeight);
    }

    /**
     * Removes a piece from the board.
     */
    public void clearPiece(Board board) {
        for (int y =0; y<pieceHeight; y++) {
            board.clearBits(boardLineIndices[y], andLines[y]);
        }
    }

    /**
     * True if the piece position is not occupied.
     */
    public boolean isFree(Board board) {
        for (int y=0; y<pieceHeight; y++) {
            if (!board.isBitsFree(boardLineIndices[y], orLines[y])) {
                return false;
            }
        }
        return true;
    }

    /**
     * True if this piece hasn't reached the bottom (down == null) and the move down
     * (current position where y is increased by one) is not occupied with filled cells.
     */
    public boolean canMoveDown(Board board) {
        return down != null && down.isFree(board);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PieceMove pieceMove = (PieceMove) o;

        if (move != null ? !move.equals(pieceMove.move) : pieceMove.move != null) return false;
        if (piece != null ? !piece.equals(pieceMove.piece) : pieceMove.piece != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = piece != null ? piece.hashCode() : 0;
        result = 31 * result + (move != null ? move.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return piece + ", " + move;
    }
}
