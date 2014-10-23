package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;

import java.util.Arrays;

import static com.github.tetrisanalyzer.game.StringUtils.format;

/**
 * Represents a Tetris board. Default size is 10x20.
 *
 * Each row is represented by a 32 bit integer where each bit corresponds to a column on the board.
 * Bit 0 corresponds to the x-value 0 (left most position), bit 1 to x-value 1 etc.
 *
 * This is a highly optimized version that does not follow best practice in object-orientation!
 */
public class Board {
    public int width;
    public int height;
    private int completeRow;
    private int[] rows;

    private static int EMPTY_ROW = 0;

    public static Board createBoard() {
        return new Board(10, 20);
    }

    public static Board createBoard(int width, int height) {
        return new Board(width, height);
    }

    /**
     * Creates a board from a string representation.
     */
    public static Board create(String... rows) {
        int width = (rows[0]).length() - 2;
        int height = rows.length - 1;

        if (!(rows[height]).equals(bottomTextRow(width))) {
            throw new IllegalArgumentException(("The bottom text row does not match the board width"));
        }
        int[] boardRows = new int[height];
        for (int y=0; y<height; y++) {
            boardRows[y] = rowsFromText(width, rows[y]);
        }
        Board result = new Board(width,  height, boardRows);

        int cells = result.numberOfOccupiedCells();
        if ((width & 1) == 0 && (cells & 1) == 1) {
            throw new IllegalArgumentException("An even board width (" + width + ") must contain an even number of filled cells (" + cells + ")");
        }
        return result;
    }

    /**
     * Creates an empty board
     */
    private Board(int width, int height) {
        this(width, height, emptyBoard(height));
    }

    private Board(int width, int height, int[] rows) {
        if (width < 4 || width > 32) {
            throw new IllegalArgumentException("The board width must be in the range 4 to 32");
        }
        if (height < 4) {
            throw new IllegalArgumentException("Minimum board height is 4");
        }
        this.width = width;
        this.height = height;
        this.rows = rows;
        completeRow = calculateCompleteRow(width);
    }

    public Board copy() {
        return new Board(width, height, copy(rows));
    }

    private static int[] emptyBoard(int height) {
        int[] rows = new int[height];
        Arrays.fill(rows, 0);
        return rows;
    }

    private static int[] copy(int[] sourceRows) {
        int[] newRows = new int[sourceRows.length];
        System.arraycopy(sourceRows, 0, newRows, 0, sourceRows.length);
        return newRows;
    }

    public static String bottomString(int chars) {
        return new String(new char[chars]).replace("\0", "¯");
    }

    public static String bottomTextRow(int width) {
        return bottomString(width + 2);
    }

    private static int rowsFromText(int width, String textRow) {
        int row = EMPTY_ROW;
        for (int x=width; x>=1; x--) {
            row <<= 1;
            row |= textRow.charAt(x) == '-' ? 0 : 1;
        }
        return row;
    }

    /**
     * Sets part of a piece on the board.
     *
     * @param y board row
     * @param pieceRowCells filled cells of a specific piece row
     */
    public void setBits(int y, int pieceRowCells) {
        rows[y] |= pieceRowCells;
    }

    /**
     * Cleares part of a piece on the board.
     *
     * @param y board row
     * @param inversePieceRowCells filled cells of a specific piece row
     */
    public void clearBits(int y, int inversePieceRowCells) {
        rows[y] &= inversePieceRowCells;
    }

    /**
     * @param y board row
     * @param pieceRowCells filled cells of a specific piece row
     * @return true if the piece row cells are not occupied on the board
     */
    public boolean isBitsFree(int y, int pieceRowCells) {
        return (rows[y] & pieceRowCells) == 0;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private int calculateCompleteRow(int width) {
        int row = EMPTY_ROW;

        for (int x=0; x<width; x++) {
            row <<= 1;
            row |= 1;
        }
        return row;
    }

    /**
     * True if the specified cell is not occupied.
     */
    public boolean isFree(int x, int y) {
        try {
            return (rows[y] & (1 << x)) == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Clear all completed rows and return number of cleared rows.
     * This method is called after a piece has been placed on the board.
     *   pieceY: the y position of the piece.
     *   pieceHeight: height of the piece.
     */
    public int clearRows(int pieceY, int pieceHeight) {
        int clearedRows = 0;
        int y1 = pieceY + pieceHeight;

        // Find first row to clear
        do {
            y1--;
            if (rows[y1] == completeRow) {
                clearedRows++;
            }
        } while (clearedRows == 0 && y1 > pieceY);

        // Clear rows
        if (clearedRows > 0) {
            int y2 = y1;

            while (y1 >= 0) {
                y2--;
                while (y2 >= pieceY && rows[y2] == completeRow) {
                    clearedRows++;
                    y2--;
                }
                if (y2 >= 0) {
                    rows[y1] = rows[y2];
                } else {
                    rows[y1] = Board.EMPTY_ROW;
                }
                y1--;
            }
        }
        return clearedRows;
    }

    /**
     * Restores this (mutable) board from a another board.
     */
    public void restore(Board other) {
        System.arraycopy(other.rows, 0, rows, 0, rows.length);
    }

    /**
     * Converts a board row into its string representation.
     */
    private String boardRowAsString(int boardRow) {
        String result = "";

        for (int i=0; i<width; i++) {
            result += (((boardRow >> i) & 1) == 0) ? "-" : "x";
        }

        return result;
    }

    public String export(String title, String tab) {
        String result = "\n  " + title + ": \n" + tab + "[";
        String separator = "";

        for (int y=0; y<height; y++) {
            result += separator + "[" + boardRowAsString(rows[y]) + "]";
            separator = "\n" + tab + " ";
        }
        return result += "]";
    }

    public boolean isBoardEmpty() {
        for (int y=0; y<height; y++) {
            if (rows[y] != EMPTY_ROW) {
                return false;
            }
        }
        return true;
    }

    public int numberOfOccupiedCells() {
        int cnt = 0;

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                if (!isFree(x, y)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * Returns the board as string ans views
     * where on the boar next piece is placed.
     *
     *   1. I: 1,10
     *  |----------|
     *  |----------|
     *  |----------|<
     *  |LL--------|
     *  |LL--------|
     *  |LLIIIIOOT-|
     *  |IJS-TTTLLL|
     *  ¯¯¯¯¯¯¯¯¯¯^¯
     */
    public String asString(long moveNo, Piece piece, Move move) {
        String board = "";

        for (int y=0; y<height; y++) {
            board += boardRowAsString(rows[y]);
            if (y == move.y) {
                board += "<";
            }
            board += "\n";
        }
        board += bottomString(move.x + 1) + "^" + bottomString(width - move.x);

        return " " + format(moveNo) + ".\n " + piece.character() + ": " + move.rotation + "," + (move.x + 1)  + "\n" + board;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (rows != null ? Arrays.hashCode(rows) : 0);

        return result;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        Board board = (Board) that;

        if (height != board.height) return false;
        if (width != board.width) return false;
        if (!Arrays.equals(rows, board.rows)) return false;

        return true;
    }

    @Override
    public String toString() {
        String board = "";
        for (int y=0; y<height; y++) {
            board += "|" + boardRowAsString(rows[y]) + "|" + "\n";
        }
        return board + bottomTextRow(width);
    }
}
