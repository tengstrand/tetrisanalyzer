package com.github.tetrisanalyzer.game

import com.github.tetrisanalyzer.BaseTest
import org.junit.Test
import com.github.tetrisanalyzer.piece.PieceS
import com.github.tetrisanalyzer.settings.DefaultGameSettings

class PositionTest extends BaseTest {

  @Test def testToString() {
    val position = new Position(new DefaultGameSettings, new PieceS);

    position.toString should be (
      "######-----SS---##\n" +
      "######----SS----##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "######----------##\n" +
      "##################\n" +
      "##################"
    )
  }
}