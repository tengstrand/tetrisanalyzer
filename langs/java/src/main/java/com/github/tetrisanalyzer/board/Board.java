package com.github.tetrisanalyzer.board;

import java.util.Arrays;

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
    private int completerOW;
    private int[] rows;

    private static int EMPTY_ROW = 0;
    private static int DEFAULT_WIDTH = 10;
    private static int DEFAULT_HEIGHT = 20;

    private static int[] getEmptyBoard(int height) {
        int[] rows = new int[height];
        Arrays.fill(rows, 0);
        return rows;
    }

    private static int[] copy(int[] sourceRows) {
        int[] newRows = new int[sourceRows.length];
        System.arraycopy(sourceRows, 0, newRows, 0, sourceRows.length);
        return newRows;
    }

    private static String getBottomTextRow(int width) {
        String boardRow = "";
        for (int x=0; x<width+2; x++) {
            boardRow += "¯";
        }
        return boardRow;
    }

    /**
     * Creates a board from a string representation.
     */
    public static Board create(String... rows) {
        int width = (rows[0]).length() - 2;
        int height = rows.length - 1;

        if (!(rows[height]).equals(getBottomTextRow(width))) {
            throw new IllegalArgumentException(("The bottom text row does not match the board length"));
        }
        int[] boardRows = new int[height];
        for (int y=0; y<height; y++) {
            boardRows[y] = getRowsFromText(width, rows[y]);
        }
        return new Board(width,  height, boardRows);
    }

    private static int getRowsFromText(int width, String textRow) {
        int row = EMPTY_ROW;
        for (int x=width; x>=1; x--) {
            row <<= 1;
            row |= textRow.charAt(x) == '-' ? 0 : 1;
        }
        return row;
    }

    /**
     * Creates an empty board with default size 10 x 20..
     */
    public Board() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, getEmptyBoard(DEFAULT_HEIGHT));
    }

    /**
     * Creates an empty board
     */
    public Board(int width, int height) {
        this(width, height, getEmptyBoard(height));
    }

    /**
     * Copy constructor
     *
     * @param board to copy
     */
    public Board(Board board) {
        this(board.width, board.height, copy(board.rows));
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
        completerOW = calculateCompleteRow(width);
    }

    /**
     * Sets part of a piece on the board.
     *
     * @param y board row
     * @param pieceRowDots dots of a piece row for a specific piece row
     */
    public void setBits(int y, int pieceRowDots) {
        rows[y] |= pieceRowDots;
    }

    /**
     * Cleares part of a piece on the board.
     *
     * @param y board row
     * @param inversePieceRowDots dots of a piece row for a specific piece row
     */
    public void clearBits(int y, int inversePieceRowDots) {
        rows[y] &= inversePieceRowDots;
    }

    /**
     * @param y board row
     * @param pieceRowDots dots of a piece row for a specific piece row
     * @return true if the piece row dots are not occupied on the board
     */
    public boolean isBitsFree(int y, int pieceRowDots) {
        return (rows[y] & pieceRowDots) == 0;
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
     * True if the specified dot is not occupied.
     */
    public boolean isFree(int x, int y) {
        try {
            return (rows[y] & (1 << x)) == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Clears completed rows and returns which rows that was cleared.
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
            if (rows[y1] == completerOW) {
                clearedRows++;
            }
        } while (clearedRows == 0 && y1 > pieceY);

        // Clear rows
        if (clearedRows > 0) {
            int y2 = y1;

            while (y1 >= 0) {
                y2--;
                while (y2 >= pieceY && rows[y2] == completerOW) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (height != board.height) return false;
        if (width != board.width) return false;
        if (!Arrays.equals(rows, board.rows)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (rows != null ? Arrays.hashCode(rows) : 0);
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

    public int numberOfDots() {
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

    @Override
    public String toString() {
        String board = "";
        for (int y=0; y<height; y++) {
            board += "|" + boardRowAsString(rows[y]) + "|" + "\n";
        }
        return board + getBottomTextRow(width);
    }
}
