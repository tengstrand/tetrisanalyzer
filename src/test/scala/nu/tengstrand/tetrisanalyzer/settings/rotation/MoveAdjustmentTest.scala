package nu.tengstrand.tetrisanalyzer.settings.rotation

import nu.tengstrand.tetrisanalyzer.BaseTest
import org.junit.Test

class MoveAdjustmentTest extends BaseTest {

  @Test def minus() {
    MoveAdjustment(0,2) - MoveAdjustment(1,1) should be (MoveAdjustment(-1,1))
  }
}