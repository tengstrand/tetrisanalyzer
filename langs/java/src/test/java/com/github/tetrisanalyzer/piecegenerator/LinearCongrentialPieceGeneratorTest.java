package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.AtariGameSettings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinearCongrentialPieceGeneratorTest {

    @Test
    public void nextPiece() {
        AtariGameSettings settings = new AtariGameSettings();
        PieceGenerator pieceGenerator = new LinearCongrentialPieceGenerator(5);

        String result = "";

        for (int i=0; i<20; i++) {
            result += pieceGenerator.nextPiece(settings);
        }

        assertEquals("TLOIJJSIJJIOZJZZTLZT", result);
    }
}