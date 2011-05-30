package com.github.tetrisanalyzer.game

trait PlayerEventReceiver {
  def setPosition(positionModel: PositionModel)
  def isReadyToReceivePosition: Boolean
}