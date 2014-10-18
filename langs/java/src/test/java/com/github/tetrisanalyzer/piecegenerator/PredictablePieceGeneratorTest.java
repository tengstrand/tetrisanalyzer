package com.github.tetrisanalyzer.piecegenerator;

import com.github.tetrisanalyzer.settings.PieceSettings;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.tetrisanalyzer.piece.Piece.S;
import static com.github.tetrisanalyzer.piece.Piece.Z;
import static com.github.tetrisanalyzer.piece.Piece.I;
import static com.github.tetrisanalyzer.piece.Piece.O;
import static org.junit.Assert.assertEquals;

public class PredictablePieceGeneratorTest {

    @Test
    public void nextPieceNumber() {
        PieceSettings settings = new TetrisAnalyzerGameSettings();
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "SZI");
        List<Integer> result = Arrays.asList(pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList(S, Z, I), result);
    }

    @Test
    public void runOutOfPieces() {
        PieceSettings settings = new TetrisAnalyzerGameSettings();
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "I");
        List<Integer> result = Arrays.asList(
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber(),
            pieceGenerator.nextPieceNumber());

        assertEquals(Arrays.asList(I, O, O, O, O), result);
    }
}
