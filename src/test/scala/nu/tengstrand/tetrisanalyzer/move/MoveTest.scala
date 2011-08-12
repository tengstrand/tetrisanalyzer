package nu.tengstrand.tetrisanalyzer.move

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test
import rotation.AnticlockwiseRotation
import nu.tengstrand.tetrisanalyzer.settings.rotation.MoveAdjustment

class MoveTest extends BaseTest {
  val move: Move = Move(0,5, 5)

  @Test def rotate {
    move.rotate(MoveAdjustment(0, 0), new AnticlockwiseRotation, 1) should be (Move(1,5, 5))
  }

  @Test def rotate_adjusted {
    move.rotate(MoveAdjustment(1, -2), new AnticlockwiseRotation, 1) should be (Move(1,6, 3))
  }

  @Test def left {
    move.left should be (Move(0,4, 5))
  }

  @Test def right {
    move.right should be (Move(0,6, 5))
  }

  @Test def up {
    move.up should be (Move(0,5, 4))
  }

  @Test def down {
    move.down should be (Move(0,5, 6))
  }

  @Test def testToString {
    move.toString should be ("(0,5, 5)")
  }
}