package tetrisanalyzer;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ValidMovesCalculatorTest {

    @Test
    public void validMoves() {
        Board board = new Board(8, 6);
        ValidMovesCalculator calculator = new ValidMovesCalculator(2, 1, board);

        Move move = new Move(0, 3, 0);
        Moves result = calculator.validMoves(move);

        Moves expected = new Moves(
            new Move(1, 1, 2),
            new Move(1, 2, 2),
            new Move(1, 3, 2),
            new Move(1, 4, 2),
            new Move(1, 5, 2),
            new Move(0, 1, 3),
            new Move(0, 2, 3),
            new Move(0, 3, 3),
            new Move(0, 4, 3)
        );

        assertEquals(expected, result);
    }
}
