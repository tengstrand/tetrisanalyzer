package nu.tengstrand.tetrisanalyzer.settings;

import nu.tengstrand.tetrisanalyzer.move.Move;
import nu.tengstrand.tetrisanalyzer.piece.Piece;
import nu.tengstrand.tetrisanalyzer.piece.PieceS;
import org.junit.Test;

public class DefaultGameSettingsTest {
    private Piece piece = new PieceS();
    private GameSettings defaultGameSettings = new DefaultGameSettings();

    @Test
    public void pieceStartMove_width10() {
      assertEquals(new Move(0, 4, 0), defaultGameSettings.pieceStartMove(10, piece));
    }

    @Test
    public void pieceStartMove_width9() {
      assertEquals(new Move(0, 3, 0), defaultGameSettings.pieceStartMove(9, piece));
    }

    @Test
    public void pieceStartMove_width8() {
      assertEquals(new Move(0,3, 0), defaultGameSettings.pieceStartMove(8, piece));
    }

    @Test
    public void pieceStartMove_width4() {
      assertEquals(new Move(0,0, 0), defaultGameSettings.pieceStartMove(4, piece));
    }
}
