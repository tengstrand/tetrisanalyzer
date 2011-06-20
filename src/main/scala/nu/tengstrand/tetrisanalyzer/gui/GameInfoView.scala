package nu.tengstrand.tetrisanalyzer.gui

import swing._
import nu.tengstrand.tetrisanalyzer.game.GameInfoReceiver

class GameInfoView extends NullPanel with GameInfoReceiver {
  val boardSize = new Label("")
  val sliding = new Label("off")
  val seed = new Label("")
  val rowsLabel = new Label("0")
  val pieces = new Label("0")
  val rowsTotalLabel = new Label("0")
  val piecesTotal = new Label("0")
  val gamesLabel = new Label("0")
  val rowsPerGame = new Label("0")
  val minRowsLabel = new Label("0")
  val maxRowsLabel = new Label("0")
  val piecesPerSec = new Label("0")
  val rowsPerSec = new Label("0")
  val time = new Label("0")
  val pause = new Label("Paused")

  val numberSeparator = new NumberSeparator

  var movesTotal = 0.0
  var rowsTotal = 0.0

  def setSeed(seed: Long) { this.seed.text = seed.toString }
  def setSliding(enabled: Boolean) { sliding.text = if (enabled) "on" else "off" }
  def setBoardSize(width: Int, height: Int) { boardSize.text = width + " x " + height }
  def setNumberOfPieces(pieces: Long) { this.pieces.text = withSpaces(pieces) }
  def setTotalNumberOfPieces(pieces: Long) { this.movesTotal = pieces.toDouble; this.piecesTotal.text = withSpaces(pieces); }
  def setNumberOfClearedRows(rows: Long) { rowsLabel.text = withSpaces(rows) }
  def setTotalNumberOfClearedRows(rows: Long) { this.rowsTotal = rows; this.rowsTotalLabel.text = withSpaces(rows) }
  def setPaused(pause: Boolean) { this.pause.text = if (pause) "Paused" else "" }
  def updateGui() { repaint() }

  def setTimePassed(seconds: Double) {
    this.time.text = calculateElapsedTime(seconds)
    this.rowsPerSec.text = withSpaces(calculatePerSec(seconds, rowsTotal))
    this.piecesPerSec.text = withSpaces(calculatePerSec(seconds, movesTotal))
  }

  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long) {
    gamesLabel.text = withSpaces(games)
    rowsPerGame.text = withSpaces(if (games == 0) 0 else totalClearedRows / games)
    minRowsLabel.text = withSpaces(minRows)
    maxRowsLabel.text = withSpaces(maxRows)
  }

  addLabel("Rows", rowsLabel, 0)
  addLabel("Pieces", pieces, 1)

  addLabel("Rows total", rowsTotalLabel, 3)
  addLabel("Pieces total", piecesTotal, 4)

  addLabel("Games", gamesLabel, 6)
  addLabel("Rows/game", rowsPerGame, 7)
  addLabel("Min rows", minRowsLabel, 8)
  addLabel("Max rows", maxRowsLabel, 9)

  addLabel("Rows/sec", rowsPerSec, 11)
  addLabel("Pieces/sec", piecesPerSec, 12)

  addLabel("Board", boardSize, 14)
  addLabel("Seed", seed, 15)
  addLabel("S[l]iding", sliding, 16)

  addLabel("Time: ", time, 18)

  addLabel("[P]ause", pause, 20)

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
