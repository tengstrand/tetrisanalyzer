package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;

/**
 * Used by the algorithm that calculates the tree of linked moves
 * with all valid moves om an empty board.
 */
public class Movement {
    public final PieceMove pieceMove;
    public final Direction direction;

    public Movement(PieceMove pieceMove) {
        this(pieceMove, Direction.DOWN);
    }

    public Movement(PieceMove pieceMove, Direction direction) {
        this.pieceMove = pieceMove;
        this.direction = direction;
    }

    public void linkTo(Movement linkedFromMovement) {
        if (direction == Direction.DOWN) {
            linkedFromMovement.pieceMove.setDown(pieceMove);
        } else {
            linkedFromMovement.pieceMove.addPieceMove(pieceMove);
        }
    }

    public Move getMove() {
        return pieceMove.move;
    }

    public int dx() {
        return pieceMove.piece.dx(pieceMove.move.rotation);
    }

    public int dy() {
        return pieceMove.piece.dy(pieceMove.move.rotation);
    }

    public int directionIndex() {
        return direction.getIndex();
    }

    private int rotationModulus() {
        return pieceMove.piece.rotationModulus();
    }

    public boolean isPieceInsideBoard(int boardWidth, int boardHeight) {
        Move move = pieceMove.move;

        return (move.x >= 0 && move.x + pieceMove.piece.width(move.rotation) <= boardWidth &&
                move.y >= 0 && move.y + pieceMove.piece.height(move.rotation) <= boardHeight);
    }

    public Movement rotate(RotationDirection rotationType, VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.move.rotate(rotationType, rotationModulus(), dx(), dy())), Direction.ROTATE);
    }

    public Movement left(VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.move.left()), Direction.LEFT);
    }

    public Movement right(VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.move.right()), Direction.RIGHT);
    }

    public Movement down(VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.move.down()), Direction.DOWN);
    }

    @Override
    public String toString() {
        return pieceMove + ", " + direction;
    }
}
