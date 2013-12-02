package tetrisanalyzer;

public class ValidMovesCalculator {
    private final int piece;
    private final int bitMask;
    private final Board board;

    public ValidMovesCalculator(int piece, int bitMask, Board board) {
        this.piece = piece;
        this.bitMask = bitMask;
        this.board = board;
    }

    public Moves validMoves(Move move) {
        return validMoves(move, new Moves(), new Moves());
    }

    public Moves validMoves(Move move, Moves validMove, Moves visited) {
        if (visited.contains(move)) {
            return new Moves();
        }
        if (board.isOccupied(move)) {
            return validMove;
        }
        Moves visitedMoves = visited.add(move);
        Moves empty = new Moves();
        Moves left = validMoves(move.left(), empty, visitedMoves);
        Moves right = validMoves(move.right(), empty, visitedMoves);
        Moves rotate = validMoves(move.rotate(), empty, visitedMoves);
        Moves down = validMoves(move.down(), new Moves(move), visitedMoves);

        return new Moves(left, right, rotate, down);
    }
}
