package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.GameSettings;

/**
 * Calculates all valid moves for a given piece and board.
 * If sliding is off (free drop), rotations and lateral movements can only occur at the first row.
 */
public class ValidPieceMoves {
    private boolean isSlidingOn;
    private Piece piece;
    private GameSettings settings;
    private VisitedPieceMoves visitedPieceMoves;
    private RotationDirection rotationDirection;

    public ValidPieceMoves(Board board, Piece piece, GameSettings settings) {
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
    public PieceMove calculateStartMove(Board board) {
        Move startMove = new Move(0, settings.pieceStartX() + piece.startX(), settings.pieceStartY + piece.startY());
        Movement startMovement = new Movement(new PieceMove(piece, startMove));
        Movement fromMovement = new Movement(new PieceMove(piece, startMove.up()));

        if (!isSlidingOn) {
            // When sliding is off, ensure that the piece can perform all valid rotations in its starting position.
            while (startMovement.pieceMove.isFree(board) && !isAllRotationsFree(startMovement, board)) {
                fromMovement = startMovement;
                startMovement = startMovement.down(visitedPieceMoves);
                markAsVisited(fromMovement, startMovement);
            }
        }

        ensureStartingPositionIsInsideBoard(startMovement, board);
        calculateValidMoves(fromMovement, startMovement, board, true);

        return startMovement.pieceMove;
    }

    /**
     * Calculate all valid moves on an empty board recursively.
     *
     * @param fromMovement the position and orientation we are coming from
     * @param movement position and orientation of current piece
     * @param firstRow true if the action "drop piece" has started
     */
    private void calculateValidMoves(Movement fromMovement, Movement movement, Board board, boolean firstRow) {
        while (visitedPieceMoves.isUnvisited(movement) && movement.isPieceInsideBoard(board)) {
            markAsVisited(fromMovement, movement);
            if (isSlidingOn || firstRow) {
                calculateValidMoves(movement, movement.rotate(rotationDirection, visitedPieceMoves), board, firstRow);
                calculateValidMoves(movement, movement.left(visitedPieceMoves), board, firstRow);
                calculateValidMoves(movement, movement.right(visitedPieceMoves), board, firstRow);
            }
            calculateValidMoves(movement, movement.down(visitedPieceMoves), board, false);
        }
    }

    private boolean isAllRotationsFree(Movement movement, Board board) {
        for (int i=0; i<=piece.rotationsEndIndex(); i++) {
            if (!movement.isPieceInsideBoard(board) || !movement.pieceMove.isFree(board)) {
                return false;
            }
            movement = movement.rotate(rotationDirection, visitedPieceMoves);
        }
        return true;
    }

    private void ensureStartingPositionIsInsideBoard(Movement startMovement, Board board) {
        if (!startMovement.isPieceInsideBoard(board)) {
            throw new IllegalStateException("The start piece position is outside the board, " +
                    "start piece settings: [" + settings.pieceStartX + "," + settings.pieceStartY + "]" +
                    ", piece: " + piece +
                    ", piece adjustment (rotation,x, y): " + startMovement.pieceMove.move);
        }
    }
}
