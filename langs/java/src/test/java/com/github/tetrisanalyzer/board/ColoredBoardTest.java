package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

import static com.github.tetrisanalyzer.board.ColoredBoard.create;
import static com.github.tetrisanalyzer.piece.Piece.createPieceL;
import static com.github.tetrisanalyzer.piece.Piece.createPieceZ;
import static junit.framework.Assert.assertEquals;

public class ColoredBoardTest {

    @Test
    public void emptyBoard() {
        ColoredBoard board = create(6, 5);

        assertEquals(board(
                "|------|",
                "|------|",
                "|------|",
                "|------|",
                "|------|",
                "========"), board.toString());
    }

    @Test
    public void boardWithPieces() {
        ColoredBoard board = create(
                "|--------|",
                "|--------|",
                "|--------|",
                "|J-----OO|",
                "|JJJ---OO|",
                "==========");

        assertEquals(board(
                "|--------|",
                "|--------|",
                "|--------|",
                "|J-----OO|",
                "|JJJ---OO|",
                "=========="), board.toString());
    }

    @Test
    public void asBoard() {
        ColoredBoard coloredBoard = create(
                "|--------|",
                "|--------|",
                "|--------|",
                "|J-----OO|",
                "|JJJ---OO|",
                "==========");
        Board board = coloredBoard.asBoard();

        assertEquals(Board.create(
                "|--------|",
                "|--------|",
                "|--------|",
                "|x-----xx|",
                "|xxx---xx|",
                "=========="
        ), board);
    }

    @Test
    public void setPiece() {
        ColoredBoard board = create(
                "|--------|",
                "|--------|",
                "|ZZ------|",
                "|JZZOO-OO|",
                "|JJJOO-OO|",
                "==========");
        Piece piece = createPieceL(new StandardGameSettings(board));
        Move move = new Move(3,4, 2);
        board.setPiece(piece, move);

        assertEquals(board(
                "|--------|",
                "|--------|",
                "|--------|",
                "|--------|",
                "|ZZ--LL--|",
                "=========="), board.toString());
    }

    @Test
    public void asString() {
        ColoredBoard board = create(
                "|--------|",
                "|--------|",
                "|--------|",
                "|--------|",
                "|x------x|",
                "==========");

        Piece piece = createPieceZ(new StandardGameSettings(board));
        Move move = new Move(0,0, 3);

        assertEquals(" Z: 0,1\n" + board(
                "|--------|",
                "|--------|",
                "|--------|",
                "|--------|",
                "|x------x|",
                "=========="), board.asString(piece, move));
    }

    @Test
    public void asString_noValidMove() {
        ColoredBoard board = create(
                "|--------|",
                "|--xxx---|",
                "|xxxxx-xx|",
                "|xx-xxxxx|",
                "|xxxxxx-x|",
                "==========");

        Piece piece = createPieceZ(new StandardGameSettings(board));
        Move move = null;

        assertEquals(" Z: -\n" + board(
                "|--------|",
                "|--xxx---|",
                "|xxxxx-xx|",
                "|xx-xxxxx|",
                "|xxxxxx-x|",
                "=========="), board.asString(piece, move));
    }

    private String board(String... rows) {
        String result = "";
        String newline = "";

        for (String row : rows) {
            result += newline + row;
            newline = "\n";
        }
        return result;
    }
}
