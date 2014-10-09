package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceS;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMovesForEmptyBoard;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.DefaultGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static junit.framework.Assert.assertTrue;

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
                if (m1.x < m2.x) { return -1; }
                if (m1.x > m2.x) { return 1; }
                if (m1.rotation < m2.rotation) { return -1; }
                if (m1.rotation < m2.rotation) { return 1; }
                if (m1.y < m2.y) { return -1; }
                return m1.y > m2.y ? 1 : 0;
            }
        });

        assertTrue(moves.contains(new Move(0,0, 2)));
        assertTrue(moves.contains(new Move(0,1, 2)));
        assertTrue(moves.contains(new Move(0,2, 2)));
        assertTrue(moves.contains(new Move(1,0, 1)));
        assertTrue(moves.contains(new Move(1,1, 1)));
        assertTrue(moves.contains(new Move(1,2, 1)));
        assertTrue(moves.contains(new Move(1,3, 1)));
    }

    private PieceMove getStartPiece(Board board) {
        Piece piece = new PieceS();
        GameSettings settings = new DefaultGameSettings();
        AllValidPieceMovesForEmptyBoard allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);

        return allValidPieceMoves.startMoveForPiece(piece);
    }
}
