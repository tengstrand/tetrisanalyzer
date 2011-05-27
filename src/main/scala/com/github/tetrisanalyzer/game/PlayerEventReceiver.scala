package com.github.tetrisanalyzer.game

trait PlayerEventReceiver {
  // TODO: Change to ColoredPosition
  def setPosition(positionModel: PositionModel)
  def isReadyToReceivePosition: Boolean
}