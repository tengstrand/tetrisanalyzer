package nu.tengstrand.tetrisanalyzer.game

trait PlayerEventReceiver {
  def setPosition(coloredPosition: ColoredPosition): Unit
  def isReadyToReceivePosition: Boolean
}