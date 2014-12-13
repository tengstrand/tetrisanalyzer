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
        this(board.width, board.height, piece, settings);
    }

    public ValidPieceMoves(int boardWidth, int boardHeight, Piece piece, GameSettings settings) {
        this.piece = piece;
        this.settings = settings;
        visitedPieceMoves = new VisitedPieceMoves(boardWidth, boardHeight, piece);
        rotationDirection = settings.rotationDirection;
        isSlidingOn = settings.isSlidingOn;
    }

    private void markAsVisited(Movement fromMovement, Movement movement) {
        visitedPieceMoves.visit(movement);
        movement.linkTo(fromMovement);
    }

    /**
     * Calculates all valid moves as a linked list of moves by returning the starting piece move for the board.
     */
    public PieceMove calculateStartMove(int boardWidth, int boardHeight) {
        Move startMove = new Move(0, startX(boardWidth), settings.pieceStartY + piece.startY());
        Movement startMovement = new Movement(new PieceMove(piece, startMove));
        Movement fromMovement = new Movement(new PieceMove(piece, startMove.up()));

        if (!isSlidingOn) {
            // When sliding is off, ensure that the piece can perform all valid rotations in its starting position.
            while (startMovement.isPieceInsideBoard(boardWidth, boardHeight) && !isAllRotationsInsideBoard(startMovement, boardWidth, boardHeight)) {
                fromMovement = startMovement;
                startMovement = startMovement.down(visitedPieceMoves);
                markAsVisited(fromMovement, startMovement);
            }
        }

        ensureStartingPositionIsInsideBoard(startMovement, boardWidth, boardHeight);
        calculateValidMoves(fromMovement, startMovement, boardWidth, boardHeight, true);

        return startMovement.pieceMove;
    }

    private int startX(int boardWidth) {
        if (boardWidth < piece.startX() + 4) {
            return 0;
        }
        return settings.pieceStartX(boardWidth) + piece.startX();
    }

    /**
     * Calculate all valid moves on an empty board recursively.
     *
     * @param fromMovement the position and orientation we are coming from
     * @param movement position and orientation of current piece
     * @param firstRow true if the action "drop piece" has started
     */
    private void calculateValidMoves(Movement fromMovement, Movement movement, int boardWidth, int boardHeight, boolean firstRow) {
        while (visitedPieceMoves.isUnvisited(movement) && movement.isPieceInsideBoard(boardWidth, boardHeight)) {
            markAsVisited(fromMovement, movement);
            if (isSlidingOn || firstRow) {
                calculateValidMoves(movement, movement.rotate(rotationDirection, visitedPieceMoves), boardWidth, boardHeight, firstRow);
                calculateValidMoves(movement, movement.left(visitedPieceMoves), boardWidth, boardHeight, firstRow);
                calculateValidMoves(movement, movement.right(visitedPieceMoves), boardWidth, boardHeight, firstRow);
            }
            calculateValidMoves(movement, movement.down(visitedPieceMoves), boardWidth, boardHeight, false);
        }
    }

    private boolean isAllRotationsInsideBoard(Movement movement, int boardWidth, int boardHeight) {
        for (int i=0; i<=piece.rotationsEndIndex(); i++) {
            if (!movement.isPieceInsideBoard(boardWidth, boardHeight)) {
                return false;
            }
            movement = movement.rotate(rotationDirection, visitedPieceMoves);
        }
        return true;
    }

    private void ensureStartingPositionIsInsideBoard(Movement startMovement, int boardWidth, int boardHeight) {
        if (!startMovement.isPieceInsideBoard(boardWidth, boardHeight)) {
            throw new IllegalStateException("The start piece position is outside the board, " +
                    "start piece settings: [" + settings.pieceStartX(boardWidth) + "," + settings.pieceStartY + "]" +
                    ", piece: " + piece +
                    ", piece adjustment (rotation,x, y): " + startMovement.pieceMove.move);
        }
    }
}
