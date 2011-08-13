package nu.tengstrand.tetrisanalyzer.settings.rotation

import nu.tengstrand.tetrisanalyzer.move.rotation.{AnticlockwiseRotation, RotationDirection}

trait DefaultRotationSettings extends RotationSettings {
  val rotationDirection = new AnticlockwiseRotation

  def rotationAdjustments = {
    Array(
      rotationAdjustment(), // 0 = empty
      rotationAdjustment((0,0), (1,0)), // 1 = I
      rotationAdjustment((0,0), (0,0)), // 2 = Z
      rotationAdjustment((0,0), (0,0)), // 3 = S
      rotationAdjustment((0,0), (0,0), (0,0), (0,0)), // 4 = J
      rotationAdjustment((0,0), (0,0), (0,0), (0,0)), // 5 = L
      rotationAdjustment((0,0), (0,0), (0,0), (0,0)), // 6 = T
      rotationAdjustment((0,0)), // 7 = O
      rotationAdjustment() // 8 = any
    )
  }
}
