package com.github.tetrisanalyzer.piecegenerator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPieceGeneratorTest {

    @Test
    public void nextPiece() {
        PieceGenerator pieceGenerator = new DefaultPieceGenerator(5);

        String result = "";

        for (int i=0; i<20; i++) {
            result += pieceGenerator.nextPiece();
        }

        assertEquals("OLIZTTSZTTZIJTJJOLJO", result);
    }
}