package com.github.tetrisanalyzer.game

trait GameInfoReceiver {
  def setPaused(paused: Boolean)
  def setPieces(pieces: Long)
  def setTotalClearedLines(lines: Long)
}