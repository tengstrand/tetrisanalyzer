package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import org.junit.Before;
import org.junit.Test;

import static com.github.tetrisanalyzer.board.Board.createBoard;
import static com.github.tetrisanalyzer.piece.Piece.createPieceS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VisitedPieceMovesTest {
    private Board board;
    private Piece piece;

    @Before
    public void setUp() {
        board = createBoard(6, 6);
        piece = createPieceS(new TetrisAnalyzerGameSettings());
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
