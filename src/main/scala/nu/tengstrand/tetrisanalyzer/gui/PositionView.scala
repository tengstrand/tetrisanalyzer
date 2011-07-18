package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.settings.ColorSettings
import java.awt._
import nu.tengstrand.tetrisanalyzer.game.{Wall, ColoredPosition, PlayerEventReceiver}

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
  private val fontChooser = new FontChooser


  def setShowRowNumbers(show: Boolean) { showRowNumbers = show }
  def toggleShowNumbers() { showNumbers = !showNumbers }
  def toggleMiniatureBoard() { smallBoard = !smallBoard }
  def isReadyToReceivePosition = position == newPosition || paused

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
        paintColumnNumbers(g)

        if (showRowNumbers)
          paintRowNumbers(g)
      }
    }
    position = newPosition
  }

  private def paintRowNumbers(g: Graphics2D) {
    g.setColor(Color.BLACK)

    val squareWidth = (columns(Wall.Left) - columns(Wall.Left - 1))

    fontChooser.setFont(squareWidth, g)

    for (number <- 1 to position.height - 2) {
      val row = number - 1
      val squareHeight = rows(row + 1) - rows(row)
      val x = columns(Wall.Left - 1) + squareWidth / 2  + (if (number < 10) -1 else -4)
      val y = rows(row) + (squareHeight-1) / 2 + 5
      g.drawString(number.toString, x, y)
    }
  }

  private def paintColumnNumbers(g: Graphics2D) {
    g.setColor(Color.BLACK)

    val squareHeight = (rows(position.height-1) - rows(position.height-2))

    fontChooser.setFont(squareHeight, g)

    val y = rows(position.height-2) + (squareHeight-1) / 2 + 5

    for (number <- 1 to position.width - Wall.Left - Wall.Right) {
      val column = number + Wall.Left - 1
      val squareWidth = columns(column + 1) - columns(column)
      val x = columns(column) + squareWidth / 2 + (if (number < 10) -2 else -5)
      g.drawString(number.toString, x, y)
    }
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
