package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.Point;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Has responsible of setting and clearing a piece on the board and can tell which
 * moves are valid on the (none empty) board.
 */
public class PieceMove {
    private int pieceHeight;
    private Board board;
    private Piece piece;
    private Move move;

    private PieceMove down;
    private Set<PieceMove> asideAndRotate = new HashSet<PieceMove>();

    private int[] boardLineIndices;
    private int[] orLines;
    private int[] andLines;

    private static final int ALL_BITS_CLEARED = 0;
    private static final int ALL_BITS_SET = -1;

    public PieceMove(Board board, Piece piece, Move move) {
        this.board = board;
        this.piece = piece;
        this.move = move;

        pieceHeight = piece.height(move.getRotation());
        orLines = new int[pieceHeight];
        andLines = new int[pieceHeight];
        Arrays.fill(orLines, ALL_BITS_CLEARED);
        Arrays.fill(andLines, ALL_BITS_SET);
        boardLineIndices = new int[pieceHeight];

        for (int y=0; y<pieceHeight; y++) {
            boardLineIndices[y] = move.getY() + y;
        }
        for (Point point: piece.getShape(move.getRotation()).getPoints()) {
            orLines[point.getY()] |= (1 << (move.getX() + point.getX()));
        }
        for (Point point: piece.getShape(move.getRotation()).getPoints()) {
            andLines[point.getY()] &= ~(1 << (move.getX() + point.getX()));
        }
    }

    public Move getMove() {
        return move;
    }

    public PieceMove getDown() {
        return down;
    }

    public void setDown(PieceMove down) {
        this.down = down;
    }

    public void addPieceMove(PieceMove pieceMove) {
        asideAndRotate.add(pieceMove);
    }

    public Set<PieceMove> getAsideAndRotate() {
        return asideAndRotate;
    }

    /**
     * Sets a piece on the board.
     *
     * @return number of cleared lines
     */
    public int setPiece() {
        for (int y=0; y<pieceHeight; y++) {
            board.setBits(boardLineIndices[y], orLines[y]);
        }
        return board.clearLines(move.getY(), pieceHeight);
    }

    /**
     * Removes a piece from the board.
     */
    public void clearPiece() {
        for (int y =0; y<pieceHeight; y++) {
            board.clearBits(boardLineIndices[y], andLines[y]);
        }
    }

    /**
     * True if the piece position is not occupied.
     */
    public boolean isFree() {
        for (int y=0; y<pieceHeight; y++) {
            if (!board.isBitsFree(boardLineIndices[y], orLines[y])) {
                return false;
            }
        }
        return true;
    }

    /**
     * True if this piece hasn't reached the bottom (down == null) and the move down
     * (current position where y is increased by one) is not occupied with "dots" (rest of pieces).
     */
    public boolean canMoveDown() {
        return down != null && down.isFree();
    }

    public Set<PieceMove> getFreeAsideAndRotateMoves() {
        Set<PieceMove> freeMoves = new HashSet<PieceMove>();

        for (PieceMove pieceMove : asideAndRotate) {
            if (pieceMove.isFree()) {
                freeMoves.add(pieceMove);
            }
        }
        return freeMoves;
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
