package com.github.tetrisanalyzer.game

import actors.Actor

trait PlayerEventReceiver extends Actor {
  def positionReceived(position: Position)

  override def act() {
    loop {
      react {
        case position: Position =>
          positionReceived(position)
      }
    }
  }
}