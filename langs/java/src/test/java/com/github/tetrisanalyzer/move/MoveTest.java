package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.move.rotation.AnticlockwiseRotation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MoveTest {
    private Move move;

    @Before
    public void setUp() {
        move = new Move(0, 5, 5);
    }

    @Test
    public void rotate() {
        assertEquals(new Move(1, 5, 5), move.rotate(new AnticlockwiseRotation(), 1, 0, 0));
    }

    @Test
    public void left() {
        assertEquals(new Move(0, 4, 5), move.left());
    }

    @Test
    public void right() {
        assertEquals(new Move(0, 6, 5), move.right());
    }

    @Test
    public void up() {
        assertEquals(new Move(0, 5, 4), move.up());
    }

    @Test
    public void down() {
        assertEquals(new Move(0, 5,6), move.down());
    }

    @Test
    public void testToString() {
        assertEquals("(0,5, 5)", move.toString());
    }
}
