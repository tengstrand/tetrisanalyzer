package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.PieceSettings;
import org.junit.Test;

import static com.github.tetrisanalyzer.piece.Piece.createPieceI;
import static com.github.tetrisanalyzer.piece.Piece.createPieceS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PieceMoveTest {
    PieceSettings settings = new AtariGameSettings();

    @Test
    public void setPiece() {
        Board board = Board.create(8, 4);
        Piece piece = createPieceS(settings);
        Move move = new Move(0,3, 1);

        new PieceMove(board, piece, move).setPiece();

        assertEquals(Board.create(
                "|--------|",
                "|----xx--|",
                "|---xx---|",
                "|--------|",
                "ŻŻŻŻŻŻŻŻŻŻ"), board);
    }

    @Test
    public void setPiece_clearTwoRows() {
        Board board = Board.create(
                "|----------|",
                "|----x-----|",
                "|xxxxxxxxxx|",
                "|-x--x----x|",
                "|xxxxxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");
        Piece piece = createPieceI(settings);
        Move move = new Move(1,4, 1);

        assertEquals(2, new PieceMove(board, piece, move).setPiece());

        assertEquals(Board.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"), board);
    }

    @Test
    public void clearPiece() {
        Board board = Board.create(
                "|xxxxxxxx|",
                "|xxxxxxxx|",
                "|xxxxxxxx|",
                "|xxxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻ");
        Piece piece = createPieceS(settings);
        Move move = new Move(0,3, 1);

        new PieceMove(board, piece, move).clearPiece();

        assertEquals(Board.create(
                "|xxxxxxxx|",
                "|xxxx--xx|",
                "xxxx--xxx|",
                "|xxxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻ"), board);
    }

    @Test
    public void isFree() {
        Board board = Board.create(
                "|-xxxxxxx|",
                "|-xxxxxxx|",
                "|xxxx--xx|",
                "|xxx--xxx|",
                "ŻŻŻŻŻŻŻŻŻŻ");
        Piece piece = createPieceS(settings);
        Move move = new Move(0,3, 2);

        assertTrue(new PieceMove(board, piece, move).isFree());
    }
}
