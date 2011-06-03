package nu.tengstrand.tetrisanalyzer.game

trait PlayerEventReceiver {
  def setPosition(positionModel: PositionModel)
  def isReadyToReceivePosition: Boolean
}