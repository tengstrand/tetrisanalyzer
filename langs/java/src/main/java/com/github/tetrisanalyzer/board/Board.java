package com.github.tetrisanalyzer.board;

import java.util.Arrays;

/**
 * Represents a Tetris board. Default size is 10x20.
 *
 * Each line is represented by a 32 bit integer where each bit corresponds to a column on the board.
 * Bit 0 corresponds to the x-value 0 (left most position), bit 1 to x-value 1 etc.
 *
 * This is a highly optimized version that does not follow best practice in object-orientation!
 */
public class Board {
    private int width;
    private int height;
    private int completeLine;
    private int[] lines;

    private static int EMPTY_LINE = 0;
    private static int DEFAULT_WIDTH = 10;
    private static int DEFAULT_HEIGHT = 20;

    private static int[] getEmptyBoard(int height) {
        int[] lines = new int[height];
        Arrays.fill(lines, 0);
        return lines;
    }

    private static int[] copy(int[] sourceLines) {
        int[] newLines = new int[sourceLines.length];
        System.arraycopy(sourceLines, 0, newLines, 0, sourceLines.length);
        return newLines;
    }

    private static String getBottomTextLine(int width) {
        String boardLine = "";
        for (int x=0; x<width+2; x++) {
            boardLine += "#";
        }
        return boardLine;
    }

    /**
     * Creates a board from a string representation.
     */
    public static Board create(String... lines) {
        int width = (lines[0]).length() - 2;
        int height = lines.length - 1;

        if (!(lines[height]).equals(getBottomTextLine(width))) {
            throw new IllegalArgumentException(("Missing bottom text line"));
        }
        int[] boardLines = new int[height];
        for (int y=0; y<height; y++) {
            boardLines[y] = getLineFromText(width, lines[y]);
        }
        return new Board(width,  height, boardLines);
    }

    private static int getLineFromText(int width, String textLine) {
        int line = EMPTY_LINE;
        for (int x=width; x>=1; x--) {
            line <<= 1;
            line |= textLine.charAt(x) == '-' ? 0 : 1;
        }
        return line;
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
        this(board.width, board.height, copy(board.lines));
    }

    private Board(int width, int height, int[] lines) {
        if (width < 4 || width > 32) {
            throw new IllegalArgumentException("The board width must be in the range 4 to 32");
        }
        if (height < 4) {
            throw new IllegalArgumentException("Minimum board height is 4");
        }
        this.width = width;
        this.height = height;
        this.lines = lines;
        completeLine = calculateCompleteLine(width);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Sets part of a piece on the board.
     *
     * @param y board row
     * @param pieceRowDots dots of a piece row for a specific piece row
     */
    public void setBits(int y, int pieceRowDots) {
        lines[y] |= pieceRowDots;
    }

    /**
     * Cleares part of a piece on the board.
     *
     * @param y board row
     * @param inversePieceRowDots dots of a piece row for a specific piece row
     */
    public void clearBits(int y, int inversePieceRowDots) {
        lines[y] &= inversePieceRowDots;
    }

    /**
     * @param y board row
     * @param pieceRowDots dots of a piece row for a specific piece row
     * @return true if the piece row dots are not occupied on the board
     */
    public boolean isBitsFree(int y, int pieceRowDots) {
        return (lines[y] & pieceRowDots) == 0;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private int calculateCompleteLine(int width) {
        int line = EMPTY_LINE;

        for (int x=0; x<width; x++) {
            line <<= 1;
            line |= 1;
        }
        return line;
    }

    /**
     * True if the specified dot is not occupied.
     */
    public boolean isFree(int x, int y) {
        try {
            return (lines[y] & (1 << x)) == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Clears completed lines and returns which lines that was cleared.
     * This method is be called after a piece has been placed on the board.
     *   pieceY: the y position of the piece.
     *   pieceHeight: height of the piece.
     */
    public int clearLines(int pieceY, int pieceHeight) {
        int clearedLines = 0;
        int y1 = pieceY + pieceHeight;

        // Find first line to clear
        do {
            y1--;
            if (lines[y1] == completeLine) {
                clearedLines++;
            }
        } while (clearedLines == 0 && y1 > pieceY);

        // Clear lines
        if (clearedLines > 0) {
            int y2 = y1;

            while (y1 >= 0) {
                y2--;
                while (y2 >= pieceY && lines[y2] == completeLine) {
                    clearedLines++;
                    y2--;
                }
                if (y2 >= 0) {
                    lines[y1] = lines[y2];
                } else {
                    lines[y1] = Board.EMPTY_LINE;
                }
                y1--;
            }
        }
        return clearedLines;
    }

    /**
     * Restores this (mutable) board from a another board.
     */
    public void restore(Board other) {
        System.arraycopy(other.lines, 0, lines, 0, lines.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (height != board.height) return false;
        if (width != board.width) return false;
        if (!Arrays.equals(lines, board.lines)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (lines != null ? Arrays.hashCode(lines) : 0);
        return result;
    }

    @Override
    public String toString() {
        String board = "";
        for (int y=0; y<height; y++) {
            board += boardLineAsString(lines[y]) + "\n";
        }
        return board + getBottomTextLine(width);
    }

    /**
     * Converts a board line into its string representation.
     */
    private String boardLineAsString(int boardLine) {
      String result = "#";

      for (int i=0; i<width; i++) {
          result += (((boardLine >> i) & 1) == 0) ? "-" : "x";
      }

      return result + "#";
    }
}
