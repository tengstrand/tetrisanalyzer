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
    private int boardWidth;
    private int boardHeight;
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
        boardWidth = board.getWidth();
        boardHeight = board.getHeight();
        visitedPieceMoves = new VisitedPieceMoves(board, piece);
        rotationDirection = settings.rotationDirection();
        isSlidingOn = settings.isSlidingEnabled();
    }

    private void markAsVisited(Movement fromMovement, Movement movement) {
        visitedPieceMoves.visit(movement);
        movement.linkTo(fromMovement);
    }

    private boolean isPieceInsideBoard(Movement movement) {
        Move move = movement.getMove();

        return (move.getX() >= 0 && move.getX() + piece.width(move.getRotation()) <= boardWidth &&
            move.getY() >= 0 && move.getY() + piece.height(move.getRotation()) <= boardHeight);
    }

    /**
     * Calculates all valid moves as a linked list of moves by returning
     * the starting piece move for the board.
     */
    public PieceMove getStartMove() {
        Move startMove = settings.pieceStartMove(boardWidth, piece);
        Movement startMovement = new Movement(new PieceMove(board, piece, startMove));
        Movement fromMovement = new Movement(new PieceMove(board, piece, startMove.up()));

        calculateValidMoves(fromMovement, startMovement);

        if (fromMovement.getPieceMove().getDown() == null) {
            throw new IllegalStateException("Illegal start position for piece");
        }
        return fromMovement.getPieceMove().getDown();
    }

    private void calculateValidMoves(Movement fromMovement, Movement movement) {
        while (visitedPieceMoves.isUnvisited(movement) && isPieceInsideBoard(movement)) {
            markAsVisited(fromMovement, movement);
            if (isSlidingOn || movement.getMove().getY() == 0) {
                calculateValidMoves(movement, movement.rotate(rotationDirection, piece.rotationModulus(), visitedPieceMoves));
                calculateValidMoves(movement, movement.left(visitedPieceMoves));
                calculateValidMoves(movement, movement.right(visitedPieceMoves));
            }
            calculateValidMoves(movement, movement.down(visitedPieceMoves));
        }
    }
}
