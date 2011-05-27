package com.github.tetrisanalyzer.gui

import java.awt.{Color, Dimension, Graphics}
import com.github.tetrisanalyzer.settings.GameSettings
import com.github.tetrisanalyzer.game.{PositionModel, PlayerEventReceiver}
import actors.Actor._
import com.github.tetrisanalyzer.game.Timer._

class PositionView(settings: GameSettings) extends DoubleBufferedView with PlayerEventReceiver {
  private val margin = 10
  private var rows = Seq.empty[Int]
  private var columns = Seq.empty[Int]
  private var position: PositionModel = null

  private var receivePosition = true

  actor {
    fiftyTimesPerSecond(() => repaint)
  }

  def isReadyToReceivePosition = receivePosition || isPaused
  private var isPaused = false

  def togglePause() { isPaused = !isPaused }

  override def setPosition(positionModel: PositionModel) {
    receivePosition = false
    position = positionModel
  }

  override def preparePaintGraphics = {
    if (position != null) {
      val squareSize = calculateSquareSize
      rows = (0 to position.height).map(y => margin + (y * squareSize).intValue)
      columns = (0 to position.width).map(x => margin + (x * squareSize).intValue)

      new Dimension(columns(position.width)-columns(0)+margin, rows(position.height)+margin-rows(0))
    } else
      new Dimension(0,0)
  }

  override def paintGraphics(g: Graphics) {
    if (position != null) {
      for (iy <- 0 until position.height) {
        for (ix <- 0 until position.width) {
          val color = position.colorValue(ix, iy)
          drawSquare(columns(ix), rows(iy), columns(ix+1), rows(iy+1),
            settings.squareColor(color),
            settings.lineColor(color), g)
        }
      }
      g.drawLine(columns(position.width)-1, rows(0), columns(position.width)-1, rows(position.height))
      g.drawLine(columns(0), rows(position.height)-1, columns(position.width), rows(position.height)-1)
      receivePosition = true
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

    if (width <= 0 || height <= 0 || position.height == 0|| position.width == 0)
      0

    if (height / width > 1.2)
      width / position.width
    else
      height / position.height
  }
}