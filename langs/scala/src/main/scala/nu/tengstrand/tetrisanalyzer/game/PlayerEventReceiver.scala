package nu.tengstrand.tetrisanalyzer.game

trait PlayerEventReceiver {
  def setPosition(coloredPosition: ColoredPosition)
  def isReadyToReceivePosition: Boolean
}