package com.github.tetrisanalyzer.piecegenerator;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tetrisanalyzer.piece.Piece.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PredictablePieceGeneratorTest {

    @Test
    public void nextPieceNumber() {
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("SZI");
        List<Integer> result = Arrays.asList(pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList(S, Z, I), result);
    }

    @Test
    public void runOutOfPieces() {
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("I");
        List<Integer> result = Arrays.asList(
                pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList(I, O, O, O, O), result);
    }

    @Test
    public void illegalPiece() {
        PieceGenerator pieceGenerator = new PredictablePieceGenerator("Sq");
        try {
            Arrays.asList(pieceGenerator.nextPieceNumber(),
                    pieceGenerator.nextPieceNumber(),
                    pieceGenerator.nextPieceNumber());
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Illegal board character 'q', expected: -OISZLJTx+", e.getMessage());
        }
    }
}
