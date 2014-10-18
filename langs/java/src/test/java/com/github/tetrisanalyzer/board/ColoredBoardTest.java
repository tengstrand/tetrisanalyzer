package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceL;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

import static com.github.tetrisanalyzer.board.Board.createBoard;
import static com.github.tetrisanalyzer.board.ColoredBoard.create;
import static junit.framework.Assert.assertEquals;

public class ColoredBoardTest {

    @Test
    public void emptyBoard() {
        ColoredBoard coloredBoard = create(6, 5);

        assertEquals(board(
                "|------|",
                "|------|",
                "|------|",
                "|------|",
                "|------|",
                "¯¯¯¯¯¯¯¯"), coloredBoard.toString());
    }

    @Test
    public void boardWithPieces() {
        ColoredBoard coloredBoard = create(
                "|--------|",
                "|--------|",
                "|--------|",
                "|J-----OO|",
                "|JJJ---OO|",
                "¯¯¯¯¯¯¯¯¯¯");

        assertEquals(board(
                "|--------|",
                "|--------|",
                "|--------|",
                "|J-----OO|",
                "|JJJ---OO|",
                "¯¯¯¯¯¯¯¯¯¯"), coloredBoard.toString());
    }

    @Test
    public void asBoard() {
        ColoredBoard coloredBoard = create(
                "|--------|",
                "|--------|",
                "|--------|",
                "|J-----OO|",
                "|JJJ---OO|",
                "¯¯¯¯¯¯¯¯¯¯");
        Board board = coloredBoard.asBoard();

        assertEquals(createBoard(
                "|--------|",
                "|--------|",
                "|--------|",
                "|x-----xx|",
                "|xxx---xx|",
                "¯¯¯¯¯¯¯¯¯¯"
        ), board);
    }

    @Test
    public void setPiece() {
        ColoredBoard coloredBoard = create(
                "|--------|",
                "|--------|",
                "|ZZ------|",
                "|JZZOO-OO|",
                "|JJJOO-OO|",
                "¯¯¯¯¯¯¯¯¯¯");
        Piece piece = new PieceL(new StandardGameSettings(coloredBoard.asBoard()));
        Move move = new Move(3,4, 2);
        coloredBoard.setPiece(piece, move);

        assertEquals(board(
                "|--------|",
                "|--------|",
                "|--------|",
                "|--------|",
                "|ZZ--LL--|",
                "¯¯¯¯¯¯¯¯¯¯"), coloredBoard.toString());
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
