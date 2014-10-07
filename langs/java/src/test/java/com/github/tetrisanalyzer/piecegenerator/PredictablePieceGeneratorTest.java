package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piece.PieceI;
import com.github.tetrisanalyzer.piece.PieceO;
import com.github.tetrisanalyzer.piece.PieceS;
import com.github.tetrisanalyzer.piece.PieceZ;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PredictablePieceGeneratorTest {

    @Test
    public void nextPieceNumber() {
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(Arrays.asList(new PieceS(), new PieceZ(), new PieceI()));
        List<Integer> result = Arrays.asList(pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList((int)new PieceS().number(), (int)new PieceZ().number(), (int)new PieceI().number()), result);
    }

    @Test
    public void runOutOfPieces() {
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(Arrays.asList((Piece)new PieceI()));
        List<Integer> result = Arrays.asList(pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList((int)new PieceI().number(), (int)new PieceO().number(), (int)new PieceO().number()), result);
    }
}
