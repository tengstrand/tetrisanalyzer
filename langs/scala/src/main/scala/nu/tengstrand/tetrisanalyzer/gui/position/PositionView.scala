package nu.tengstrand.tetrisanalyzer.gui.position

import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import nu.tengstrand.tetrisanalyzer.game.{ColoredPosition, PlayerEventReceiver}
import java.awt._

class PositionView(colorSettings: ColorSettings) extends PlayerEventReceiver {
  private val backgroundColor = new Color(250, 250, 250)

  private val paused = true
  private var showNumbers = false
  private var showRowNumbers = false
  private var smallBoard = false

  private val Margin = 10
  private var rows = Seq.empty[Int]
  private var columns = Seq.empty[Int]
  private var position: Option[ColoredPosition] = None
  private var newPosition: Option[ColoredPosition] = None

  private var squareSize = 0.0
  private val squareSizeCalculator = new SquareSizeCalculator

  private val rowNumbersPainter = new RowNumbersPainter
  private val columnNumbersPainter = new ColumnNumbersPainter

  def setShowRowNumbers(show: Boolean): Unit = { showRowNumbers = show }
  def showNumbers(show: Boolean): Unit = { showNumbers = show }
  def toggleMiniatureBoard(): Unit = { smallBoard = !smallBoard }
  def isReadyToReceivePosition = position == newPosition || paused

  def forceSetPosition(coloredPosition: ColoredPosition): Unit = {
    position = Some(coloredPosition)
    newPosition = Some(coloredPosition)
  }

  override def setPosition(coloredPosition: ColoredPosition): Unit = {
    newPosition = Some(coloredPosition)

    if (position.isEmpty)
      position = newPosition
  }

  def preparePaintGraphics(size: Dimension): Dimension = {
    position match {
      case Some(pos) =>
        squareSize = squareSizeCalculator.calculateSquareSize(smallBoard, size.width-350, size.height-20, pos.width, pos.height)
        rows = (0 to pos.height).map(y => Margin + (y * squareSize).intValue)
        columns = (0 to pos.width).map(x => Margin + (x * squareSize).intValue)

        new Dimension(columns(pos.width)-columns(0)+Margin, rows(pos.height)+Margin-rows(0))
      case None =>
        new Dimension(0, 0)
    }
  }

  def paintGraphics(size: Dimension, g: Graphics2D): Unit = {
    g.setColor(backgroundColor)

    position.foreach { pos =>
      for (iy <- 0 until pos.height) {
        for (ix <- 0 until pos.width) {
          val colorValue = pos.colorValue(ix, iy)
          paintSquare(columns(ix), rows(iy), columns(ix+1), rows(iy+1),
            colorSettings.squareColor(colorValue),
            colorSettings.lineColor(colorValue), g)
        }
      }
      g.drawLine(columns(pos.width)-1, rows(0), columns(pos.width)-1, rows(pos.height)-1)
      g.drawLine(columns(0), rows(pos.height)-1, columns(pos.width)-1, rows(pos.height)-1)
      g.drawLine(columns(6), rows(0), columns(6), rows(pos.height-2)-1)

      if (squareSize > 6 && showNumbers) {
        columnNumbersPainter.paintNumbers(pos, rows, columns, g)

        if (showRowNumbers)
          rowNumbersPainter.paintNumbers(pos, rows, columns, g)
      }
    }
    position = newPosition
  }

  private def paintSquare(x1: Int, y1: Int, x2: Int, y2: Int, squareColor: Color, lineColor: Color, g: Graphics): Unit = {
    g.setColor(squareColor)
    g.fillRect(x1+1, y1+1, x2-x1-1, y2-y1-1)
    g.setColor(lineColor)
    g.drawLine(x1, y1, x1, y2-1)
    g.drawLine(x1, y1, x2-1, y1)
  }
}
