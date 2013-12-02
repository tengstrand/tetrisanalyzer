package tetrisanalyzer;

public class Move {
    public final int rotation;
    public final int x;
    public final int y;

    public Move(int rotation, int x, int y) {
        this.rotation = rotation;
        this.x = x;
        this.y = y;
    }

    public Move left() {
        return new Move(rotation, x - 1, y);
    }

    public Move right() {
        return new Move(rotation, x + 1, y);
    }

    public Move rotate() {
        return new Move(((rotation+1) & 1), x, y);
    }

    public Move down() {
        return new Move(rotation, x, y + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (rotation != move.rotation) return false;
        if (x != move.x) return false;
        if (y != move.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rotation;
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "rotation=" + rotation +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
