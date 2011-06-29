package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.game.GameInfoReceiver
import java.awt._

object GameInfoView {
  val OrigoX = 420
}

trait GameInfoView extends DoubleBufferedView with GameInfoReceiver {
  private val textFont = new Font("Monospaced", Font.PLAIN, 14);

  private var seed = 0L
  private var slidingEnabled = false
  private var boardSize: Dimension = new Dimension(10, 20)
  private var pieces = 0L
  private var piecesTotal = 0L
  private var clearedRows = 0L
  private var clearedRowsTotal = 0L

  private var games = 0L
  private var minRows = 0L
  private var maxRows = 0L

  private var secondsPassed = 0.0

  val numberSeparator = new NumberSeparator

  def isPaused: Boolean

  def setSeed(seed: Long) { this.seed = seed }
  def setSliding(enabled: Boolean) { slidingEnabled = enabled }
  def setBoardSize(width: Int, height: Int) { boardSize = new Dimension(width, height) }
  def setNumberOfPieces(pieces: Long) { this.pieces = pieces }
  def setTotalNumberOfPieces(piecesTotal: Long) { this.piecesTotal = pieces }
  def setNumberOfClearedRows(clearedRows: Long) { this.clearedRows = clearedRows }
  def setTotalNumberOfClearedRows(clearedRowsTotal: Long) { this.clearedRowsTotal = clearedRowsTotal }
  def updateGui() { repaint() }
  def setTimePassed(seconds: Double) { secondsPassed = seconds }

  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long) {
    this.games = games
    this.minRows = minRows
    this.maxRows = maxRows
    this.clearedRowsTotal = totalClearedRows
  }

  abstract override def preparePaintGraphics: Dimension = {
    super.preparePaintGraphics
  }

  abstract override def paintGraphics(graphics: Graphics) {
    super.paintGraphics(graphics)

    val g = graphics.asInstanceOf[Graphics2D];

    g.setFont(textFont);
    g.setColor(Color.BLACK);

    drawInfo("Rows:", withSpaces(clearedRows), 1, g)
    drawInfo("Pieces:", withSpaces(pieces), 2, g)

    drawInfo("Rows total:", withSpaces(clearedRowsTotal), 4, g)
    drawInfo("Pieces total:", withSpaces(piecesTotal), 5, g)

    drawInfo("Games:", withSpaces(games), 7, g)
    drawInfo("Rows/game:", withSpaces(if (games == 0) 0 else clearedRowsTotal / games), 8, g)
    drawInfo("Min rows:", minRows, 9, g)
    drawInfo("Max rows:", maxRows, 10, g)

    drawInfo("Rows/sec:", withSpaces(calculateUnitsPerSec(secondsPassed, clearedRowsTotal)), 12, g)
    drawInfo("Pieces/sec:", withSpaces(calculateUnitsPerSec(secondsPassed, piecesTotal)), 13, g)

    drawInfo("Board:", boardSize.width + " x " + boardSize.height, 15, g)
    drawInfo("S[e]ed:", seed, 16, g)
    drawInfo("S[l]iding:", if (slidingEnabled) "On" else "Off", 17, g)

    drawInfo("Elapsed time:", calculateElapsedTime(secondsPassed), 19, g)

    drawInfo("[P]ause:", if (isPaused) "On" else "", 21, g)
  }

  private def drawInfo(label: String, value: Any, row: Int, g: Graphics2D) {
    val y = 15 + row * 20
    g.drawString(label, 10 + GameInfoView.OrigoX, y)
    drawValue(value.toString, y, g)
  }

  private def drawValue(value: String, y: Int, g: Graphics2D) {
    g.drawString(value, 240 - value.length * 8 + GameInfoView.OrigoX, y)
  }

  private def withSpaces(number: Long) = numberSeparator.withSpaces(number)

  private def calculateElapsedTime(seconds: Double): String = {
    val sec: Int = (seconds*10 % 600).toInt
    val min: Int = (seconds/60 % 60).toInt
    val hours: Int = (seconds/3600).toInt
    hours + "h " + min + "m " + (sec/10) + "." + (sec%10) + "s"
  }

  private def calculateUnitsPerSec(seconds: Double, total: Double): Long = {
    if (seconds == 0 || total == 0) {
      0
    } else {
      val unitsPerSecond = scala.math.round(total / seconds);
      unitsPerSecond.toLong
    }
  }
}