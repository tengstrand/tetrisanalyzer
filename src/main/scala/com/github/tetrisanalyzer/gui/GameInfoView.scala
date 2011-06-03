package com.github.tetrisanalyzer.gui

import swing._
import com.github.tetrisanalyzer.game.GameInfoReceiver

class GameInfoView extends NullPanel with GameInfoReceiver {
  val lines = new Label("0")
  val pieces = new Label("0")
  val linesTotal = new Label("0")
  val piecesTotal = new Label("0")
  val gamesLabel = new Label("0")
  val linesPerGame = new Label("0")
  val minLinesLabel = new Label("0")
  val maxLinesLabel = new Label("0")
  val piecesPerSec = new Label("0")
  val time = new Label("0")
  val pause = new Label("Paused")

  val numberSeparator = new NumberSeparator

  var moves = 0L
  var movesTotal = 0.0

  var games = 0
  var totalLinesPerGame = 0L
  var minLines = 0L
  var maxLines = 0L

  def setNumberOfPieces(pieces: Long) { this.moves = pieces; this.pieces.text = withSpaces(pieces) }
  def setTotalNumberOfPieces(pieces: Long) { this.movesTotal = pieces.toDouble; this.piecesTotal.text = withSpaces(pieces); }
  def setNumberOfClearedLines(lines: Long) { this.lines.text = withSpaces(lines) }
  def setTotalNumberOfClearedLines(lines: Long) { this.linesTotal.text = withSpaces(lines) }
  def setPaused(pause: Boolean) { this.pause.text = if (pause) "Paused" else "-" }
  def updateGui() { repaint() }

  def setTimePassed(seconds: Double) {
    this.time.text = calculateElapsedTime(seconds)
    this.piecesPerSec.text = withSpaces(calculatePiecesPerSec(seconds))
  }

  def setNumberOfGamesAndLinesInLastGame(games: Int, lines: Long) {
    this.games = games
    totalLinesPerGame += lines
    if (minLines == 0 || lines < minLines)
      minLines = lines

    if (maxLines == 0 || lines > maxLines)
      maxLines = lines

    this.gamesLabel.text = withSpaces(games.toLong)
    linesPerGame.text = withSpaces(totalLinesPerGame / games)
    minLinesLabel.text = withSpaces(minLines)
    maxLinesLabel.text = withSpaces(maxLines)
  }

  addLabel("Lines", lines, 0)
  addLabel("Pieces", pieces, 1)

  addLabel("Lines total", linesTotal, 3)
  addLabel("Pieces total", piecesTotal, 4)

  addLabel("Games", gamesLabel, 6)
  addLabel("Lines/game", linesPerGame, 7)
  addLabel("Min lines", minLinesLabel, 8)
  addLabel("Max lines", maxLinesLabel, 9)

  addLabel("Pieces/sec", piecesPerSec, 11)

  addLabel("Time: ", time, 13)

  addLabel("[P]ause", pause, 15)

  private def addLabel(text: String, label: Label, row: Int) {
    val y = 10 + row * 20
    add(new Label(text + ":"), new Rectangle(10,y, 100,20))
    add(label, new Rectangle(100,y, 100,20))
  }

  private def withSpaces(number: Long) = numberSeparator.withSpaces(number)

  private def calculateElapsedTime(seconds: Double): String = {
    val sec: Int = (seconds*10 % 600).toInt
    val min: Int = (seconds/60 % 60).toInt
    val hours: Int = (seconds/3600).toInt
    hours + "h " + min + "m " + (sec/10) + "." + (sec%10) + "s"
  }

  private def calculatePiecesPerSec(seconds: Double): Long = {
    if (seconds == 0 || movesTotal == 0) {
      0
    } else {
      val piecesPerSecond = scala.math.round(movesTotal / seconds);
      piecesPerSecond.toLong
    }
  }
}
