package tetrisanalyzer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Moves {
    private final Set<Move> moves;

    public Moves(Move move, Move... moves) {
        this.moves = new HashSet<Move>();
        this.moves.add(move);
        this.moves.addAll(Arrays.asList(moves));
    }

    public Moves(Moves... movesList) {
        this.moves = new HashSet<Move>();

        for (Moves moves : movesList) {
            this.moves.addAll(moves.moves);
        }
    }

    public boolean contains(Move move) {
        return moves.contains(move);
    }

    public Moves add(Move move) {
        Moves result = new Moves();
        result.moves.addAll(moves);
        result.moves.add(move);

        return result;
    }

    @Override
    public int hashCode() {
        return moves != null ? moves.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Moves moves1 = (Moves) o;

        if (moves != null ? !moves.equals(moves1.moves) : moves1.moves != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Moves{" +
                "totalPieces=" + moves +
                '}';
    }
}
