package tetrisanalyzer;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void isOccupied() {
        Board board = new Board(8, 6);

        assertTrue(board.isOccupied(new Move(0, 0, 0)));
        assertFalse(board.isOccupied(new Move(0, 1, 0)));
        assertFalse(board.isOccupied(new Move(0, 4, 0)));
        assertTrue(board.isOccupied(new Move(0, 5, 0)));
        assertFalse(board.isOccupied(new Move(1, 5, 0)));
        assertTrue(board.isOccupied(new Move(1, 6, 0)));
        assertFalse(board.isOccupied(new Move(0, 1, 3)));
        assertTrue(board.isOccupied(new Move(0, 1, 4)));

        assertFalse(board.isOccupied(new Move(1, 1, 2)));
        assertTrue(board.isOccupied(new Move(1, 1, 3)));
    }
}
