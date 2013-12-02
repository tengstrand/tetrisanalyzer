package tetrisanalyzer;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private final int width;
    private final int height;
    private final Set<Square> squares;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;

        squares = new HashSet<Square>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == width - 1 || y == height - 1) {
                    squares.add(new Square(y, x, 9));
                } else {
                    squares.add(new Square(y, x, 0));
                }
            }
        }
    }

    /**
     * Fakes a check of a Z-piece on an empty board!
     */
    public boolean isOccupied(Move move) {
        return !(move.x > 0 && ((move.x - move.rotation + 3) < width)
                && (move.y + move.rotation + 2 < height));
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (squares != null ? squares.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (height != board.height) return false;
        if (width != board.width) return false;
        if (squares != null ? !squares.equals(board.squares) : board.squares != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Board{" +
                "width=" + width +
                ", height=" + height +
                ", squares=" + squares +
                '}';
    }
}
