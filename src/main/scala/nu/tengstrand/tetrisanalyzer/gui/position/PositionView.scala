package nu.tengstrand.tetrisanalyzer.gui.position

import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import java.awt._
import nu.tengstrand.tetrisanalyzer.game.{ColoredPosition, PlayerEventReceiver}
class PositionView(colorSettings: ColorSettings) extends PlayerEventReceiver {
  private val backgroundColor = new Color(250, 250, 250)

  private val paused = true
  private var showNumbers = false
  private var showRowNumbers = false
  private var smallBoard = false

  private val Margin = 10
  private var rows = Seq.empty[Int]
  private var columns = Seq.empty[Int]
  private var position: ColoredPosition = null
  private var newPosition: ColoredPosition = null

  private var squareSize = 0.0

  private val rowNumberPainter = new RowNumberPainter
  private val columnNumberPainter = new ColumnNumberPainter

  def setShowRowNumbers(show: Boolean) { showRowNumbers = show }
  def showNumbers(show: Boolean) { showNumbers = show }
  def toggleMiniatureBoard() { smallBoard = !smallBoard }
  def isReadyToReceivePosition = position == newPosition || paused

  def forceSetPosition(coloredPosition: ColoredPosition) {
    position = coloredPosition
    newPosition = coloredPosition
  }

  override def setPosition(coloredPosition: ColoredPosition) {
    newPosition = coloredPosition

    if (position == null)
      position = coloredPosition
  }

  def preparePaintGraphics(size: Dimension): Dimension = {
    if (position != null) {
      squareSize = calculateSquareSize(size)
      rows = (0 to position.height).map(y => Margin + (y * squareSize).intValue)
      columns = (0 to position.width).map(x => Margin + (x * squareSize).intValue)

      new Dimension(columns(position.width)-columns(0)+Margin, rows(position.height)+Margin-rows(0))
    } else {
      new Dimension(0, 0)
    }
  }

  def paintGraphics(size: Dimension, g: Graphics2D) {
    g.setColor(backgroundColor)

    if (position != null) {
      for (iy <- 0 until position.height) {
        for (ix <- 0 until position.width) {
          val colorValue = position.colorValue(ix, iy)
          paintSquare(columns(ix), rows(iy), columns(ix+1), rows(iy+1),
            colorSettings.squareColor(colorValue),
            colorSettings.lineColor(colorValue), g)
        }
      }
      g.drawLine(columns(position.width)-1, rows(0), columns(position.width)-1, rows(position.height)-1)
      g.drawLine(columns(0), rows(position.height)-1, columns(position.width)-1, rows(position.height)-1)
      g.drawLine(columns(6), rows(0), columns(6), rows(position.height-2)-1)

      if (squareSize > 6 && showNumbers) {
        columnNumberPainter.paintNumbers(position, rows, columns, g)

        if (showRowNumbers)
          rowNumberPainter.paintNumbers(position, rows, columns, g)
      }
    }
    position = newPosition
  }

  private def paintSquare(x1: Int, y1: Int, x2: Int, y2: Int, squareColor: Color, lineColor: Color, g: Graphics) {
    g.setColor(squareColor)
    g.fillRect(x1+1, y1+1, x2-x1-1, y2-y1-1)
    g.setColor(lineColor)
    g.drawLine(x1, y1, x1, y2-1)
    g.drawLine(x1, y1, x2-1, y1)
  }

  private def calculateSquareSize(size: Dimension): Double = {
    if (smallBoard)
      6
    else {
      val width = size.width - 20.0
      val height = size.height - 20.0

      if (width <= 0 || height <= 0 || position.height == 0|| position.width == 0)
        0

      if (position.height / position.width.toDouble < 1.15)
        (width * 0.6) / position.width
      else
        (height / position.height)
    }
  }
}
