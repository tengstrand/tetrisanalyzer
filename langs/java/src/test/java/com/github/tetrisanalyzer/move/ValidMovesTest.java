package nu.tengstrand.tetrisanalyzer.move;

import nu.tengstrand.tetrisanalyzer.board.Board;
import nu.tengstrand.tetrisanalyzer.piece.Piece;
import nu.tengstrand.tetrisanalyzer.piece.PieceS;
import nu.tengstrand.tetrisanalyzer.piecemove.AllValidPieceMovesForEmptyBoard;
import nu.tengstrand.tetrisanalyzer.piecemove.PieceMove;
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings;
import nu.tengstrand.tetrisanalyzer.settings.GameSettings;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ValidMovesTest {

    @Test public void pieceMoves() {
        Board board = new Board(5,4);
        ValidMoves validMoves = new ValidMoves(board);
        PieceMove startPiece = getStartPiece(board);
        List<Move> moves = new ArrayList<Move>();

        for (PieceMove pieceMove : validMoves.getPieceMoves(startPiece)) {
            moves.add(pieceMove.getMove());
        }
        Collections.sort(moves, new Comparator<Move>() {
            public int compare(Move m1, Move m2) {
                if (m1.getX() < m2.getX()) { return -1; }
                if (m1.getX() > m2.getX()) { return 1; }
                if (m1.getRotation() < m2.getRotation()) { return -1; }
                if (m1.getRotation() < m2.getRotation()) { return 1; }
                if (m1.getY() < m2.getY()) { return -1; }
                return m1.getY() > m2.getY() ? 1 : 0;
            }
        });

        assertEquals(Arrays.asList(
                new Move(0,0, 2),
                new Move(1,0, 1),
                new Move(0,1, 2),
                new Move(1,1, 1),
                new Move(0,2, 2),
                new Move(1,2, 1),
                new Move(1,3, 1)), moves);
    }

    private PieceMove getStartPiece(Board board) {
        Piece piece = new PieceS();
        GameSettings settings = new DefaultGameSettings();
        AllValidPieceMovesForEmptyBoard allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);

        return allValidPieceMoves.startMoveForPiece(piece);
    }
}
