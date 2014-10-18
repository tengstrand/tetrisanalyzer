package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.Point;

import java.util.Arrays;

import static com.github.tetrisanalyzer.board.Board.bottomTextRow;
import static com.github.tetrisanalyzer.board.Board.createBoard;

public class ColoredBoard {
    public final int width;
    public final int height;
    final char[][] board;

    private final char EMPTY_DOT = '-';
    private final String WALL_DOT = "|";

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

        if (!(rows[height]).equals(bottomTextRow(width))) {
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

    public ColoredBoard copy() {
        ColoredBoard copy = new ColoredBoard(width, height);

        for (int y=0; y<height; y++) {
            System.arraycopy(board[y], 0, copy.board[y], 0, width);
        }
        return copy;
    }

    public Board asBoard() {
        return createBoard(asStringRows(WALL_DOT, true));
    }

    public void setPiece(Piece piece, Move move) {
        int pieceHeight = piece.height(move.rotation);

        for (Point point : piece.getShape(move.rotation).getPoints()) {
            board[move.y + point.y][move.x + point.x] = piece.character();
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

    public String export(String title, String tab) {
        String result = "\n  " + title + ": \n" + tab + "[";
        String separator = "";

        for (String row : asStringRows("", false)) {
            result += separator + "[" + row + "]";
            separator = "\n" + tab + " ";
        }
        return result + "]";
    }

    private String[] asStringRows(String wall, boolean withBottomRow) {
        String[] result = new String[height + (withBottomRow ? 1 : 0)];

        for (int y=0; y<height; y++) {
            result[y] = wall;
            for (int x=0; x<width; x++) {
                result[y] += board[y][x];
            }
            result[y] += wall;
        }
        if (withBottomRow) {
            result[height] = bottomTextRow(width);
        }

        return result;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;

        for (int y=0; y<height; y++) {
            result = 31 * result + Arrays.hashCode(board[y]);
        }
        return result;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        ColoredBoard board = (ColoredBoard) that;

        if (height != board.height) return false;
        if (width != board.width) return false;

        for (int y=0; y<height; y++) {
            if (!Arrays.equals(this.board[y], board.board[y]))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        String separator = "";

        for (String row : asStringRows(WALL_DOT, true)) {
            result += separator + row;
            separator = "\n";
        }
        return result;
    }
}