package tetrisanalyzer;

public class Square {
    private final int y;
    private final int x;
    private final int p;

    public Square(int y, int x, int p) {
        this.y = y;
        this.x = x;
        this.p = p;
    }

    @Override
    public int hashCode() {
        int result = y;
        result = 31 * result + x;
        result = 31 * result + p;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        if (p != square.p) return false;
        if (x != square.x) return false;
        if (y != square.y) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Square{" +
                "y=" + y +
                ", x=" + x +
                ", p=" + p +
                '}';
    }
}
