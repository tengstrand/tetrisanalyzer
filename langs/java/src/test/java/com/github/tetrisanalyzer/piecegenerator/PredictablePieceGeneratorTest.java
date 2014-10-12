package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.piece.PieceI;
import com.github.tetrisanalyzer.piece.PieceO;
import com.github.tetrisanalyzer.piece.PieceS;
import com.github.tetrisanalyzer.piece.PieceZ;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import com.github.tetrisanalyzer.settings.PieceSettings;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PredictablePieceGeneratorTest {

    @Test
    public void nextPieceNumber() {
        PieceSettings settings = new TetrisAnalyzerGameSettings();
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, new PieceS(settings), new PieceZ(settings), new PieceI(settings));
        List<Integer> result = Arrays.asList(pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList((int)new PieceS(settings).number(), (int)new PieceZ(settings).number(), (int)new PieceI(settings).number()), result);
    }

    @Test
    public void runOutOfPieces() {
        PieceSettings settings = new TetrisAnalyzerGameSettings();
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, new PieceI(settings));
        List<Integer> result = Arrays.asList(pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList((int)new PieceI(settings).number(), (int)new PieceO(settings).number(), (int)new PieceO(settings).number()), result);
    }
}
