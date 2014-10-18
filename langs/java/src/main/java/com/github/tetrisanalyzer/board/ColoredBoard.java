package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;

public class ColoredBoard {
    final int width;
    final int height;
    final char[][] board;

    private final char EMPTY_DOT = '-';

    private ColoredBoard(int width, int height) {
        this.width = width;
        this.height = height;
        board = new char[height][];

        for (int y=0; y<height; y++) {
            board[y] = new char[width];
            clearRow(y);
        }
    }

    public static ColoredBoard create(int width, int height) {
        return new ColoredBoard(width, height);
    }

    public static ColoredBoard create(String... rows) {
        int width = (rows[0]).length() - 2;
        int height = rows.length - 1;

        if (!(rows[height]).equals(Board.getBottomTextRow(width))) {
            throw new IllegalArgumentException(("The bottom text row does not match the board width"));
        }

        ColoredBoard board = new ColoredBoard(width, height);

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                board.board[y][x] = rows[y].charAt(x + 1);
            }
        }
        return board;
    }

    public Board asBoard() {
        return Board.create(asStringRows());
    }

    public void setPiece(Piece piece, Move move) {
        int pieceHeight = piece.height(move.rotation);
        int pieceWidth = piece.width(move.rotation);

        for (int y=0; y<pieceHeight; y++) {
            for (int x=0; x<pieceWidth; x++) {
                board[move.y + y][move.x + x] = piece.character();
            }
        }
        clearRows(move.y, pieceHeight);
    }

    /**
     * Clear all completed rows.
     *   pieceY: the y position of the piece.
     *   pieceHeight: height of the piece.
     */
    private void clearRows(int pieceY, int pieceHeight) {
        int clearedRows = 0;
        int y1 = pieceY + pieceHeight;

        // Find first row to clear (if any)
        do {
            y1--;
            if (isRowComplete(y1)) {
                clearedRows++;
            }
        } while (clearedRows == 0 && y1 > pieceY);

        // Clear rows
        if (clearedRows > 0) {
            int y2 = y1;

            while (y1 >= 0) {
                y2--;
                while (y2 >= pieceY && isRowComplete(y2)) {
                    y2--;
                }
                if (y2 >= 0) {
                    copyRow(y1, y2);
                } else {
                    clearRow(y1);
                }
                y1--;
            }
        }
    }

    private void copyRow(int to, int from) {
        for (int x=0; x<width; x++) {
            board[to][x] = board[from][x];
        }
    }

    private void clearRow(int y) {
        for (int x=0; x<width; x++) {
            board[y][x] = EMPTY_DOT;
        }
    }

    private boolean isRowComplete(int y) {
        for (int x=0; x<width; x++) {
            if (board[y][x] == EMPTY_DOT) {
                return false;
            }
        }
        return true;
    }

    private String[] asStringRows() {
        String[] result = new String[height + 1];

        for (int y=0; y<height; y++) {
            String row = "|";
            for (int x=0; x<width; x++) {
                row += board[y][x];
            }
            result[y] = row + "|";
        }
        result[height] = Board.getBottomTextRow(width);

        return result;
    }

    @Override
    public String toString() {
        String result = "";
        String separator = "";

        for (String row : asStringRows()) {
            result += separator + row;
            separator = "\n";
        }
        return result;
    }
}
