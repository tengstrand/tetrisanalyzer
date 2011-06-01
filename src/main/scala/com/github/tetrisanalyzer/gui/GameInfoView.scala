package com.github.tetrisanalyzer.gui

import swing._
import com.github.tetrisanalyzer.game.GameInfoReceiver

class GameInfoView extends NullPanel with GameInfoReceiver {
  val piecesLabel = new Label("0")
  val lines = new Label("0")
  val piecesPerSec = new Label("-")
  val pause = new Label("On")

  val numberSeparator = new NumberSeparator

  var pieces = 0.0

  def setPieces(pieces: Long) { this.pieces = pieces.toDouble; this.piecesLabel.text = withSpaces(pieces); repaint() }
  def setTotalClearedLines(lines: Long) { this.lines.text = withSpaces(lines) }
  def setTimePassed(seconds: Double) { this.piecesPerSec.text = withSpaces(calculatePiecesPerSec(seconds)) }
  def setPaused(pause: Boolean) { this.pause.text = if (pause) "On" else "Off" }

  add(new Label("Pieces:"), new Rectangle(10,10, 70,20))
  add(piecesLabel, new Rectangle(100,10, 70,20))

  add(new Label("Lines:"), new Rectangle(10,30, 70,20))
  add(lines, new Rectangle(100,30, 70,20))

  add(new Label("Pieces/sec:"), new Rectangle(10,50, 70,20))
  add(piecesPerSec, new Rectangle(100,50, 70,20))

  add(new Label("[P]ause:"), new Rectangle(10,90, 70,15))
  add(pause, new Rectangle(100,90, 70,15))

  private def withSpaces(number: Long) = numberSeparator.withSpaces(number)

  private def calculatePiecesPerSec(seconds: Double): Long = {
    if (seconds == 0 || piecesLabel == 0) {
      0
    } else {
      val piecesPerSecond = scala.math.round(pieces / seconds);
      piecesPerSecond.toLong
    }
  }
}
