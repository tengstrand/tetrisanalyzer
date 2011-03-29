package com.github.tetrisanalyzer.settings

import org.junit.{Before, Test}
import com.github.tetrisanalyzer.BaseTest
import com.github.tetrisanalyzer.piece.PieceS
import com.github.tetrisanalyzer.move.Move

class DefaultGameSettingsTest extends BaseTest {
  var piece = new PieceS
  var defaultGameSettings: GameSettings = null

  @Before def setUp {
    defaultGameSettings = new DefaultGameSettings
  }

  @Test def pieceStartMove_width10() {
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