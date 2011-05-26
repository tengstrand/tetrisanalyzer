package com.github.tetrisanalyzer.game

import actors.Actor

case class PauseMessage(paused: Boolean)
case class GameInfoMessage(pieces: Long, clearedLines: Long)

trait GameInfoReceiver extends Actor {
  def setPause(pause: Boolean)
  def setPieces(pieces: Long)
  def setTotalClearedLines(lines: Long)

  override def act() {
    loop {
      react {
        case PauseMessage(paused) =>
          setPause(paused)
        case GameInfoMessage(pieces, clearedLines) =>
          setPieces(pieces)
          setTotalClearedLines(clearedLines)
      }
    }
  }
}