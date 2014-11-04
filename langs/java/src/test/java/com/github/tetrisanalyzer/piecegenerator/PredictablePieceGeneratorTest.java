package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.PieceSettings;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tetrisanalyzer.piece.Piece.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PredictablePieceGeneratorTest {

    @Test
    public void nextPieceNumber() {
        PieceSettings settings = new AtariGameSettings();
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "SZI");
        List<Integer> result = Arrays.asList(pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber(),
                pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList(S, Z, I), result);
    }

    @Test
    public void runOutOfPieces() {
        PieceSettings settings = new AtariGameSettings();
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "I");
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
        PieceSettings settings = new AtariGameSettings();
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "Sq");
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
