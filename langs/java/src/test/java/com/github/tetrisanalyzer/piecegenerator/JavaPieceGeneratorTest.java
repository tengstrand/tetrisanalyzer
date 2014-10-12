package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JavaPieceGeneratorTest {

    @Test
    public void nextPiece() {
        PieceGenerator pieceGenerator = new JavaPieceGenerator(new TetrisAnalyzerGameSettings());

        for (int i=1; i<=1000; i++) {
            int pieceNumber = pieceGenerator.nextPiece().number();

            assertTrue(pieceNumber >= 1);
            assertTrue(pieceNumber <= 7);
        }
    }
}
