package nu.tengstrand.tetrisanalyzer.gui

import swing._
import nu.tengstrand.tetrisanalyzer.game.GameInfoReceiver

class GameInfoView extends NullPanel with GameInfoReceiver {
  val boardSize = new Label("")
  val seed = new Label("")
  val linesLabel = new Label("0")
  val pieces = new Label("0")
  val linesTotalLabel = new Label("0")
  val piecesTotal = new Label("0")
  val gamesLabel = new Label("0")
  val linesPerGame = new Label("0")
  val minLinesLabel = new Label("0")
  val maxLinesLabel = new Label("0")
  val piecesPerSec = new Label("0")
  val linesPerSec = new Label("0")
  val time = new Label("0")
  val pause = new Label("Paused")

  val numberSeparator = new NumberSeparator

  var movesTotal = 0.0
  var linesTotal = 0.0

  def setSeed(seed: Long) { this.seed.text = seed.toString }
  def setBoardSize(width: Int, height: Int) { boardSize.text = width + " x " + height }
  def setNumberOfPieces(pieces: Long) { this.pieces.text = withSpaces(pieces) }
  def setTotalNumberOfPieces(pieces: Long) { this.movesTotal = pieces.toDouble; this.piecesTotal.text = withSpaces(pieces); }
  def setNumberOfClearedLines(lines: Long) { linesLabel.text = withSpaces(lines) }
  def setTotalNumberOfClearedLines(lines: Long) { this.linesTotal = lines; this.linesTotalLabel.text = withSpaces(lines) }
  def setPaused(pause: Boolean) { this.pause.text = if (pause) "Paused" else "" }
  def updateGui() { repaint() }

  def setTimePassed(seconds: Double) {
    this.time.text = calculateElapsedTime(seconds)
    this.linesPerSec.text = withSpaces(calculatePerSec(seconds, linesTotal))
    this.piecesPerSec.text = withSpaces(calculatePerSec(seconds, movesTotal))
  }

  def setNumberOfGamesAndLinesInLastGame(games: Long, lines: Long, totalClearedLines: Long, minLines: Long, maxLines: Long) {
    gamesLabel.text = withSpaces(games)
    linesPerGame.text = withSpaces(if (games == 0) 0 else totalClearedLines / games)
    minLinesLabel.text = withSpaces(minLines)
    maxLinesLabel.text = withSpaces(maxLines)
  }

  addLabel("Lines", linesLabel, 0)
  addLabel("Pieces", pieces, 1)

  addLabel("Lines total", linesTotalLabel, 3)
  addLabel("Pieces total", piecesTotal, 4)

  addLabel("Games", gamesLabel, 6)
  addLabel("Lines/game", linesPerGame, 7)
  addLabel("Min lines", minLinesLabel, 8)
  addLabel("Max lines", maxLinesLabel, 9)

  addLabel("Lines/sec", linesPerSec, 11)
  addLabel("Pieces/sec", piecesPerSec, 12)

  addLabel("Board", boardSize, 14)
  addLabel("Seed", seed, 15)

  addLabel("Time: ", time, 17)

  addLabel("[P]ause", pause, 19)

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

  private def calculatePerSec(seconds: Double, total: Double): Long = {
    if (seconds == 0 || total == 0) {
      0
    } else {
      val piecesPerSecond = scala.math.round(total / seconds);
      piecesPerSecond.toLong
    }
  }
}
