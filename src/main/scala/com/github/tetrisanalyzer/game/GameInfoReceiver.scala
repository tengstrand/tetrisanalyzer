package com.github.tetrisanalyzer.game

trait GameInfoReceiver {
  def setPause(pause: Boolean)
  def setPieces(pieces: Long)
  def setTotalClearedLines(lines: Long)
}