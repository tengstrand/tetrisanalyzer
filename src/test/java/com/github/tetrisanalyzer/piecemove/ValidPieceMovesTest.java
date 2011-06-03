package nu.tengstrand.tetrisanalyzer.piecemove;

import nu.tengstrand.tetrisanalyzer.board.Board;
import nu.tengstrand.tetrisanalyzer.move.Move;
import nu.tengstrand.tetrisanalyzer.piece.Piece;
import nu.tengstrand.tetrisanalyzer.piece.PieceO;
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class ValidPieceMovesTest {

    @Test
    public void getStartMove() {
        Board board = new Board(5,5);
        Piece piece = new PieceO();
        ValidPieceMovesForEmptyBoard validPieceMoves = new ValidPieceMovesForEmptyBoard(board, piece, new DefaultGameSettings());
        PieceMove startMove = validPieceMoves.getStartMove();

        assertEquals(new PieceMove(board, piece, new Move(0,1, 0)), startMove);

        Set<PieceMove> expectedMoves = new HashSet<PieceMove>();
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 0)));

        assertEquals(expectedMoves, startMove.getAsideAndRotateMoves());

        assertEquals(new PieceMove(board, piece, new Move(0,1, 1)), startMove.getDown());
    }
}
