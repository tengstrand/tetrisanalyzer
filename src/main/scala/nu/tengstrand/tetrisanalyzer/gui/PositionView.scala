package nu.tengstrand.tetrisanalyzer.gui

import java.awt.{Color, Dimension, Graphics}
import nu.tengstrand.tetrisanalyzer.game.{ColoredPosition, PlayerEventReceiver}
import nu.tengstrand.tetrisanalyzer.settings.ColorSettings

class PositionView(colorSettings: ColorSettings) extends PlayerEventReceiver {
  private val backgroundColor = new Color(240, 240, 240)

  private val paused = true

  private val Margin = 10
  private var rows = Seq.empty[Int]
  private var columns = Seq.empty[Int]
  private var position: ColoredPosition = null
  private var newPosition: ColoredPosition = null

  def isReadyToReceivePosition = position == newPosition || paused

  override def setPosition(coloredPosition: ColoredPosition) {
    newPosition = coloredPosition

    if (position == null)
      position = coloredPosition
  }

  def preparePaintGraphics(size: Dimension): Dimension = {
    if (position != null) {
      val squareSize = calculateSquareSize(size)
      rows = (0 to position.height).map(y => Margin + (y * squareSize).intValue)
      columns = (0 to position.width).map(x => Margin + (x * squareSize).intValue)

      new Dimension(columns(position.width)-columns(0)+Margin, rows(position.height)+Margin-rows(0))
    } else {
      new Dimension(0, 0)
    }
  }

  def paintGraphics(size: Dimension, g: Graphics) {
    g.setColor(backgroundColor)
    g.fillRect(0, 0, size.width, size.height)

    if (position != null) {
      for (iy <- 0 until position.height) {
        for (ix <- 0 until position.width) {
          val colorValue = position.colorValue(ix, iy)
          drawSquare(columns(ix), rows(iy), columns(ix+1), rows(iy+1),
            colorSettings.squareColor(colorValue),
            colorSettings.lineColor(colorValue), g)
        }
      }
      g.drawLine(columns(position.width)-1, rows(0), columns(position.width)-1, rows(position.height))
      g.drawLine(columns(0), rows(position.height)-1, columns(position.width), rows(position.height)-1)
    }
    position = newPosition
  }

  def drawSquare(x1: Int, y1: Int, x2: Int, y2: Int, squareColor: Color, lineColor: Color, g: Graphics) {
    g.setColor(squareColor)
    g.fillRect(x1, y1, x2-x1, y2-y1)
    g.setColor(lineColor)
    g.drawLine(x1, y1, x1, y2)
    g.drawLine(x1, y1, x2, y1)
  }

  private def calculateSquareSize(size: Dimension): Double = {
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
