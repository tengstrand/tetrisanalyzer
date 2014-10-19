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
        boardWidth = board.width;
        boardHeight = board.height;
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

        return (move.x >= 0 && move.x + piece.width(move.rotation) <= boardWidth &&
            move.y >= 0 && move.y + piece.height(move.rotation) <= boardHeight);
    }

    /**
     * Calculates all valid moves as a linked list of moves by returning
     * the starting piece move for the board.
     */
    public PieceMove getStartMove() {
        Move startMove = new Move(0, settings.pieceStartX() + piece.startX(), settings.pieceStartY + piece.startY());
        Movement startMovement = new Movement(new PieceMove(board, piece, startMove));
        Movement fromMovement = new Movement(new PieceMove(board, piece, startMove.up()));

        if (startMove.x < 0 || startMove.y < 0 ||
                startMove.x + piece.width(startMove.rotation) > board.width ||
                startMove.y + piece.height(startMove.rotation) > board.height) {
            throw new IllegalStateException("The start piece position is outside the board, " +
                    "start piece settings: [" + settings.pieceStartX + "," + settings.pieceStartY + "]" +
                    ", piece: " + piece +
                    ", piece adjustment (rotation,x, y): " + startMove);
        }

        calculateValidMoves(fromMovement, startMovement, true);

        return startMovement.pieceMove;
    }

    private void calculateValidMoves(Movement fromMovement, Movement movement, boolean isFirstRow) {
        while (visitedPieceMoves.isUnvisited(movement) && isPieceInsideBoard(movement)) {
            markAsVisited(fromMovement, movement);
            if (isSlidingOn || isFirstRow || movement.isAdjusted()) {
                calculateValidMoves(movement, movement.rotate(rotationDirection, piece.rotationModulus(), movement.dx(), movement.dy(), visitedPieceMoves), isFirstRow);
                calculateValidMoves(movement, movement.left(visitedPieceMoves), isFirstRow);
                calculateValidMoves(movement, movement.right(visitedPieceMoves), isFirstRow);
            }
            calculateValidMoves(movement, movement.down(visitedPieceMoves), false);
        }
    }
}
