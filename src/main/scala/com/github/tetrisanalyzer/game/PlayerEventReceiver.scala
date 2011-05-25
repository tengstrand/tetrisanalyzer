package com.github.tetrisanalyzer.game

import actors.Actor

trait PlayerEventReceiver extends Actor {
  def positionReceived(positionModel: PositionModel)
  def readyToReceivePosition: Boolean

  override def act() {
    loop {
      react {
        case positionModel: PositionModel =>
          positionReceived(positionModel)
      }
    }
  }
}