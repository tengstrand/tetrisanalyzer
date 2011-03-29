package com.github.tetrisanalyzer.move

import com.github.tetrisanalyzer.BaseTest
import org.junit.{Before, Test}
import rotation.AnticlockwiseRotation

class MoveTest extends BaseTest {
  var move: Move = _

  @Before def setUp {
    move = Move(0,5, 5)
  }

  @Test def rotate {
    move.rotate(new AnticlockwiseRotation, 1) should be (Move(1,5, 5))
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