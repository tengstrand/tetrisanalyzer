package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.AtariGameSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinearCongrentialPieceGeneratorTest {

    @Test
    public void nextPiece() {
        PieceGenerator pieceGenerator = new LinearCongrentialPieceGenerator(5, new AtariGameSettings());

        String result = "";

        for (int i=0; i<20; i++) {
            result += pieceGenerator.nextPiece();
        }

        assertEquals("TLOIJJSIJJIOZJZZTLZT", result);
    }
}