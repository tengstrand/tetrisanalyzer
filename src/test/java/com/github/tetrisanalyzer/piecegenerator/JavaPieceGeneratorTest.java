package nu.tengstrand.tetrisanalyzer.piecegenerator;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JavaPieceGeneratorTest {

    @Test
    public void nextPiece() {
        PieceGenerator pieceGenerator = new JavaPieceGenerator();

        for (int i=1; i<=1000; i++) {
            int pieceNumber = pieceGenerator.nextPiece().number();

            assertTrue(pieceNumber >= 1);
            assertTrue(pieceNumber <= 7);
        }
    }
}
