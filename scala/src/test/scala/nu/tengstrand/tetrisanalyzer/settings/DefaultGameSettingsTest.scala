package nu.tengstrand.tetrisanalyzer.settings

import org.junit.Test
import nu.tengstrand.tetrisanalyzer.BaseTest
import nu.tengstrand.tetrisanalyzer.piece.PieceS
import nu.tengstrand.tetrisanalyzer.move.Move

class DefaultGameSettingsTest extends BaseTest {
  var piece = PieceS()
  var defaultGameSettings = new DefaultGameSettings

  @Test def pieceStartMove_width10PieceS() {
    defaultGameSettings.pieceStartMove(10, piece) should be (Move(0,4, 0))
  }

  @Test def pieceStartMove_width9() {
    defaultGameSettings.pieceStartMove(9, piece) should be (Move(0,3, 0))
  }

  @Test def pieceStartMove_width8() {
    defaultGameSettings.pieceStartMove(8, piece) should be (Move(0,3, 0))
  }

  @Test def pieceStartMove_width4() {
    defaultGameSettings.pieceStartMove(4, piece) should be (Move(0,0, 0))
  }
}