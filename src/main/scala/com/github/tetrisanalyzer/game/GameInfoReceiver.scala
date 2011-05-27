package com.github.tetrisanalyzer.game

case class PauseMessage(paused: Boolean)
case class GameInfoMessage(pieces: Long, clearedLines: Long)

trait GameInfoReceiver {
  def setPause(pause: Boolean)
  def setPieces(pieces: Long)
  def setTotalClearedLines(lines: Long)
}