package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.GameSettings;

/**
 * Calculates all valid moves for a given piece on an empty board.
 * If sliding is on, rotations and lateral movements can only occur in the top row.
 */
public class ValidPieceMovesForEmptyBoard {
    private boolean isSlidingOn;
    private Board board;
    private Piece piece;
    private GameSettings settings;
    private VisitedPieceMoves visitedPieceMoves;
    private RotationDirection rotationDirection;

    public ValidPieceMovesForEmptyBoard(Board board, Piece piece, GameSettings settings) {
        this.board = board;
        this.piece = piece;
        this.settings = settings;
        visitedPieceMoves = new VisitedPieceMoves(board, piece);
        rotationDirection = settings.rotationDirection();
        isSlidingOn = settings.isSlidingEnabled();
    }

    private void markAsVisited(Movement fromMovement, Movement movement) {
        visitedPieceMoves.visit(movement);
        movement.linkTo(fromMovement);
    }

    /**
     * Calculates all valid moves as a linked list of moves by returning the starting piece move for the board.
     */
    public PieceMove calculateStartMove() {
        Move startMove = new Move(0, settings.pieceStartX() + piece.startX(), settings.pieceStartY + piece.startY());
        Movement startMovement = new Movement(new PieceMove(board, piece, startMove));
        Movement fromMovement = new Movement(new PieceMove(board, piece, startMove.up()));

        if (!isSlidingOn) {
            // When sliding is off, ensure that the piece can perform all valid rotations in its starting position.
            while (startMovement.pieceMove.isFree() && !isAllRotationsFree(startMovement)) {
                fromMovement = startMovement;
                startMovement = startMovement.down(visitedPieceMoves);
                markAsVisited(fromMovement, startMovement);
            }
        }

        ensureStartingPositionIsInsideBoard(startMovement);
        calculateValidMoves(fromMovement, startMovement, true);

        return startMovement.pieceMove;
    }

    /**
     * Calculate all valid moves on an empty board recursively.
     *
     * @param fromMovement the position and orientation we are coming from
     * @param movement position and orientation of current piece
     * @param firstRow true if the action "drop piece" has started
     */
    private void calculateValidMoves(Movement fromMovement, Movement movement, boolean firstRow) {
        while (visitedPieceMoves.isUnvisited(movement) && movement.isPieceInsideBoard()) {
            markAsVisited(fromMovement, movement);
            if (isSlidingOn || firstRow) {
                calculateValidMoves(movement, movement.rotate(rotationDirection, visitedPieceMoves), firstRow);
                calculateValidMoves(movement, movement.left(visitedPieceMoves), firstRow);
                calculateValidMoves(movement, movement.right(visitedPieceMoves), firstRow);
            }
            calculateValidMoves(movement, movement.down(visitedPieceMoves), false);
        }
    }

    private boolean isAllRotationsFree(Movement movement) {
        for (int i=0; i<=piece.rotationsEndIndex(); i++) {
            if (!movement.isPieceInsideBoard() || !movement.pieceMove.isFree()) {
                return false;
            }
            movement = movement.rotate(rotationDirection, visitedPieceMoves);
        }
        return true;
    }

    private void ensureStartingPositionIsInsideBoard(Movement startMovement) {
        if (!startMovement.isPieceInsideBoard()) {
            throw new IllegalStateException("The start piece position is outside the board, " +
                    "start piece settings: [" + settings.pieceStartX + "," + settings.pieceStartY + "]" +
                    ", piece: " + piece +
                    ", piece adjustment (rotation,x, y): " + startMovement.pieceMove.move);
        }
    }
}
