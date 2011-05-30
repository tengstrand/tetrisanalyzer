package com.github.tetrisanalyzer.gui

import swing._
import com.github.tetrisanalyzer.game.GameInfoReceiver

class GameInfoView extends NullPanel with GameInfoReceiver {
  val pause = new Label("On")
  val pieces = new Label("0")
  val lines = new Label("0")

  val numberSeparator = new NumberSeparator

  def setPaused(pause: Boolean) { this.pause.text = if (pause) "On" else "Off" }
  def setPieces(pieces: Long) { this.pieces.text = withSpaces(pieces); repaint() }
  def setTotalClearedLines(lines: Long) { this.lines.text = withSpaces(lines) }

  add(new Label("[P]ause:"), new Rectangle(10,10, 50,10))
  add(pause, new Rectangle(100,10, 50,10))

  add(new Label("Pieces:"), new Rectangle(10,30, 50,10))
  add(pieces, new Rectangle(100,30, 50,10))

  add(new Label("Lines:"), new Rectangle(10,50, 50,10))
  add(lines, new Rectangle(100,50, 50,10))

  private def withSpaces(number: Long) = numberSeparator.withSpaces(number)
}
