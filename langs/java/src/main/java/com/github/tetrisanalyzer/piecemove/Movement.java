package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.move.rotation.RotationDirection;

/**
 * Used by the algorithm that calculates the tree of linked moves
 * with all valid moves om an empty board.
 */
public class Movement {
    private PieceMove pieceMove;
    private Direction direction;

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
        return pieceMove.getMove();
    }

    public PieceMove getPieceMove() {
        return pieceMove;
    }

    public int getDirectionIndex() {
        return direction.getIndex();
    }

    public Movement rotate(RotationDirection rotationType, int rotationModulus, VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.getMove().rotate(rotationType, rotationModulus)), Direction.ROTATE);
    }

    public Movement left(VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.getMove().left()), Direction.LEFT);
    }

    public Movement right(VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.getMove().right()), Direction.RIGHT);
    }

    public Movement down(VisitedPieceMoves visitedPieceMoves) {
        return new Movement(visitedPieceMoves.get(pieceMove.getMove().down()), Direction.DOWN);
    }

    @Override
    public String toString() {
        return pieceMove + ", " + direction;
    }
}
