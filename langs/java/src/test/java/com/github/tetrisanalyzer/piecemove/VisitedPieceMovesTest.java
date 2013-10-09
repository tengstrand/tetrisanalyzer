package nu.tengstrand.tetrisanalyzer.piecemove;

import nu.tengstrand.tetrisanalyzer.board.Board;
import nu.tengstrand.tetrisanalyzer.move.Move;
import nu.tengstrand.tetrisanalyzer.piece.Piece;
import nu.tengstrand.tetrisanalyzer.piece.PieceS;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VisitedPieceMovesTest {
    private Board board;
    private Piece piece;

    @Before
    public void setUp() {
        board = new Board(6,6);
        piece = new PieceS();
    }

    @Test
    public void visitLeft() {
        Move move = new Move(0,0,0);
        VisitedPieceMoves visitedMoves = new VisitedPieceMoves(board, piece);
        visitedMoves.visit(getMovement(move, Direction.LEFT));

        assertFalse(visitedMoves.isUnvisited(getMovement(move, Direction.LEFT)));
        assertTrue(visitedMoves.isUnvisited(getMovement(move, Direction.RIGHT)));
        assertTrue(visitedMoves.isUnvisited(getMovement(move, Direction.DOWN)));
        assertFalse(visitedMoves.isUnvisited(getMovement(move, Direction.ROTATE)));
    }

    private Movement getMovement(Move move, Direction direction) {
        PieceMove pieceMove = new PieceMove(board, piece, move);
        return new Movement(pieceMove, direction);
    }
}