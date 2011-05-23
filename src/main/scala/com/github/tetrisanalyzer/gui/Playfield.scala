package com.github.tetrisanalyzer.gui

import java.awt.{Color, Dimension, Graphics}
import com.github.tetrisanalyzer.game.{Position, PlayerEventReceiver}
import com.github.tetrisanalyzer.settings.GameSettings

class Playfield(settings: GameSettings) extends DoubleBufferedComponent with PlayerEventReceiver {
  val margin = 10
  var rows = Seq.empty[Int]
  var columns = Seq.empty[Int]
  private var position: Position = null

  var readyToReceivePosition = true

  def positionReceived(position: Position) {
    readyToReceivePosition = false
    this.position = position
  }

  def preparePaintGraphics = {
    if (position != null) {
      val squareSize = calculateSquareSize
      rows = (0 to position.playfieldHeight).map(y => margin + (y * squareSize).intValue)
      columns = (0 to position.playfieldWidth).map(x => margin + (x * squareSize).intValue)

      new Dimension(columns(position.playfieldWidth)-columns(0)+margin-1, rows(position.playfieldHeight)+margin-rows(0)-1)
    } else
      new Dimension(0,0)
  }

  def paintGraphics(g: Graphics) {
    if (position != null) {
      for (iy <- 0 until position.playfieldHeight) {
        for (ix <- 0 until position.playfieldWidth) {
          val color = position.colorValue(ix, iy)
          drawSquare(columns(ix), rows(iy), columns(ix+1), rows(iy+1),
            settings.squareColor(color),
            settings.lineColor(color), g)
        }
      }
      readyToReceivePosition = true
    }
  }

  def drawSquare(x1: Int, y1: Int, x2: Int, y2: Int, squareColor: Color, lineColor: Color, g: Graphics) {
    g.setColor(squareColor)
    g.fillRect(x1, y1, x2, y2)
    g.setColor(lineColor)
    g.drawLine(x1, y1, x1, y2)
    g.drawLine(x1, y1, x2, y1)
  }

  private def calculateSquareSize: Double = {
    val width = this.size.width - 20.0
    val height = this.size.height - 20.0

    if (width <= 0 || height <= 0)
      0

    if (height / width > 2)
      width / position.playfieldWidth
    else
      height / position.playfieldHeight
  }
}