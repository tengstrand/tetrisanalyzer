package nu.tengstrand.tetrisanalyzer.settings.rotation

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import nu.tengstrand.tetrisanalyzer.piece.{PieceT, PieceI}

class DefaultRotationSettingsTest extends BaseTest {
  val rotationSettings = new DefaultRotationSettings {}

  @Test def adjustment_pieceT_rotationZero(): Unit = {
    val adjustment = rotationSettings.rotationAdjustment(PieceT(), 0)

    adjustment should  be (MoveAdjustment(0, 0))
  }

  @Test def adjustment_pieceI_rotationZero(): Unit = {
    val adjustment = rotationSettings.rotationAdjustment(PieceI(), 0)

    adjustment should  be (MoveAdjustment(1, 0))
  }

  @Test def adjustment_pieceI_rotationOne(): Unit = {
    val adjustment = rotationSettings.rotationAdjustment(PieceI(), 1)

    adjustment should  be (MoveAdjustment(-1, 0))
  }
}
